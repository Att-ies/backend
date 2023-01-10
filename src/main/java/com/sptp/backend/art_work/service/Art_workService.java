package com.sptp.backend.art_work.service;

import com.sptp.backend.art_work.repository.Art_work;
import com.sptp.backend.art_work.repository.Art_workRepository;
import com.sptp.backend.art_work.web.dto.request.Art_workSaveRequestDto;
import com.sptp.backend.art_work_image.repository.Art_work_image;
import com.sptp.backend.art_work_image.repository.Art_work_imageRepository;
import com.sptp.backend.art_work_keyword.repository.Art_work_keyword;
import com.sptp.backend.art_work_keyword.repository.Art_work_keywordRepository;
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
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Art_workService extends BaseEntity {

    private final Art_workRepository art_workRepository;
    private final Art_work_keywordRepository art_work_keywordRepository;
    private final Art_work_imageRepository art_work_imageRepository;
    private final MemberRepository memberRepository;
    private final AwsService awsService;
    private final FileService fileService;

    @Transactional
    public void saveArt_work(Long loginMemberId, Art_workSaveRequestDto dto) throws IOException {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        checkExistsImage(dto);

        String GuaranteeImageUUID = UUID.randomUUID().toString();
        String GuaranteeImageEXT = fileService.extractExt(dto.getGuaranteeImage().getOriginalFilename());

        Art_work art_work = Art_work.builder()
                .member(findMember)
                .title(dto.getTitle())
                .material(dto.getMaterial())
                .size(dto.getSize())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .guaranteeImage(GuaranteeImageUUID + "." + GuaranteeImageEXT)
                .build();

        art_workRepository.save(art_work);
        awsService.uploadImage(dto.getGuaranteeImage(), GuaranteeImageUUID);
        saveArtImages(dto.getImage(), art_work);
        saveArtKeywords(dto.getKeywords(), art_work);
    }

    public void saveArtImages(MultipartFile[] files, Art_work art_work) throws IOException {

        for (MultipartFile file : files) {

            String imageUUID = UUID.randomUUID().toString();
            String imageEXT = fileService.extractExt(file.getOriginalFilename());

            Art_work_image art_work_image = Art_work_image.builder()
                    .art_work(art_work)
                    .image(imageUUID + "." + imageEXT)
                    .build();

            art_work_imageRepository.save(art_work_image);
            awsService.uploadImage(file, imageUUID);
        }
    }

    public void saveArtKeywords(String[] keywords, Art_work art_work) {

        for (String keyword : keywords) {

            Art_work_keyword art_work_keyword = Art_work_keyword.builder()
                    .art_work(art_work)
                    .keywordId(KeywordMap.map.get(keyword))
                    .build();

            art_work_keywordRepository.save(art_work_keyword);
        }
    }

    public void checkExistsImage(Art_workSaveRequestDto dto) {
        if (dto.getGuaranteeImage().isEmpty() || dto.getImage()[0].isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PARAM);
        }
    }
}
