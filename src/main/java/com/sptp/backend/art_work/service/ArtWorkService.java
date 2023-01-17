package com.sptp.backend.art_work.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.art_work.web.dto.request.ArtWorkSaveRequestDto;
import com.sptp.backend.art_work.web.dto.response.ArtWorkInfoResponseDto;
import com.sptp.backend.art_work_image.repository.ArtWorkImage;
import com.sptp.backend.art_work_image.repository.ArtWorkImageRepository;
import com.sptp.backend.art_work_keyword.repository.ArtWorkKeyword;
import com.sptp.backend.art_work_keyword.repository.ArtWorkKeywordRepository;
import com.sptp.backend.aws.service.AwsService;
import com.sptp.backend.aws.service.FileService;
import com.sptp.backend.common.KeywordMap;
import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtWorkService extends BaseEntity {

    private final ArtWorkRepository artWorkRepository;
    private final ArtWorkKeywordRepository artWorkKeywordRepository;
    private final ArtWorkImageRepository artWorkImageRepository;
    private final MemberRepository memberRepository;
    private final AwsService awsService;
    private final FileService fileService;

    @Transactional
    public void saveArtWork(Long loginMemberId, ArtWorkSaveRequestDto dto) throws IOException {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        checkExistsImage(dto);

        String GuaranteeImageUUID = UUID.randomUUID().toString();
        String GuaranteeImageEXT = fileService.extractExt(dto.getGuaranteeImage().getOriginalFilename());

        String mainImageUUID = UUID.randomUUID().toString();
        String mainImageEXT = fileService.extractExt(dto.getImage()[0].getOriginalFilename());

        ArtWork artWork = ArtWork.builder()
                .member(findMember)
                .title(dto.getTitle())
                .material(dto.getMaterial())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .statusDescription(dto.getStatusDescription())
                .guaranteeImage(GuaranteeImageUUID + "." + GuaranteeImageEXT)
                .mainImage(mainImageUUID + "." + mainImageEXT)
                .genre(dto.getGenre())
                .size(dto.getSize())
                .height(dto.getHeight())
                .length(dto.getLength())
                .width(dto.getWidth())
                .frame(dto.isFrame())
                .description(dto.getDescription())
                .productionYear(dto.getProductionYear())
                .build();

        artWorkRepository.save(artWork);
        awsService.uploadImage(dto.getGuaranteeImage(), GuaranteeImageUUID);
        awsService.uploadImage(dto.getImage()[0], mainImageUUID);
        saveArtImages(dto.getImage(), artWork);
        saveArtKeywords(dto.getKeywords(), artWork);
    }

    public void saveArtImages(MultipartFile[] files, ArtWork artWork) throws IOException {

        for (MultipartFile file : files) {

            String imageUUID = UUID.randomUUID().toString();
            String imageEXT = fileService.extractExt(file.getOriginalFilename());

            ArtWorkImage artWorkImage = ArtWorkImage.builder()
                    .artWork(artWork)
                    .image(imageUUID + "." + imageEXT)
                    .build();

            artWorkImageRepository.save(artWorkImage);
            awsService.uploadImage(file, imageUUID);
        }
    }

    public void saveArtKeywords(String[] keywords, ArtWork artWork) {

        for (String keyword : keywords) {

            KeywordMap.checkExistsKeyword(keyword);

            ArtWorkKeyword artWorkKeyword = ArtWorkKeyword.builder()
                    .artWork(artWork)
                    .keywordId(KeywordMap.map.get(keyword))
                    .build();

            artWorkKeywordRepository.save(artWorkKeyword);
        }
    }

    public void checkExistsImage(ArtWorkSaveRequestDto dto) {
        if (dto.getGuaranteeImage().isEmpty() || dto.getImage()[0].isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_EXIST_IMAGE);
        }
    }

    public ArtWorkInfoResponseDto getArtWork(Long artWorkId) {

        ArtWork findArtWork = artWorkRepository.findById(artWorkId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        Member findArtist = memberRepository.findById(findArtWork.getMember().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTIST));

        ArtWorkInfoResponseDto artWorkInfoResponseDto = ArtWorkInfoResponseDto.builder()
                .title(findArtWork.getTitle())
                .artistName(findArtist.getNickname())
                .artistEducation(findArtist.getEducation())
                .productionYear(findArtWork.getProductionYear())
                .material(findArtWork.getMaterial())
                .genre(findArtWork.getGenre())
                .frame(findArtWork.isFrame())
                .width(findArtWork.getWidth())
                .height(findArtWork.getHeight())
                .length(findArtWork.getLength())
                .size(findArtWork.getSize())
                .description(findArtWork.getDescription())
                .artWorkImage(findArtWork.getMainImage())
                .artistImage(findArtist.getImage())
                .build();

        return artWorkInfoResponseDto;
    }
}
