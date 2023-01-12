package com.sptp.backend.member.service;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.aws.service.AwsService;
import com.sptp.backend.aws.service.FileService;
import com.sptp.backend.member.web.dto.request.*;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.jwt.web.dto.TokenDto;
import com.sptp.backend.jwt.service.JwtService;
import com.sptp.backend.member.web.dto.response.ArtistResponse;
import com.sptp.backend.member.web.dto.response.MemberLoginResponseDto;
import com.sptp.backend.member.web.dto.response.MemberResponse;
import com.sptp.backend.common.KeywordMap;
import com.sptp.backend.member_preferred_artist.repository.MemberPreferredArtist;
import com.sptp.backend.member_preferred_artist.repository.MemberPreferredArtistRepository;
import com.sptp.backend.member_preffereed_art_work.repository.MemberPreferredArtWork;
import com.sptp.backend.member_preffereed_art_work.repository.MemberPreferredArtWorkRepository;
import com.sptp.backend.memberkeyword.repository.MemberKeyword;
import com.sptp.backend.memberkeyword.repository.MemberKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final RedisTemplate redisTemplate;
    private final MemberKeywordRepository memberKeywordRepository;
    private final FileService fileService;
    private final AwsService awsService;
    private final MemberPreferredArtistRepository memberPreferredArtistRepository;
    private final ArtWorkRepository artWorkRepository;
    private final MemberPreferredArtWorkRepository memberPreferredArtWorkRepository;

    @Value("${aws.storage.url}")
    private String awsStorageUrl;

    @Transactional
    public Member saveUser(MemberSaveRequestDto dto) {

        checkDuplicateMemberUserID(dto.getUserId());
        checkDuplicateMemberEmail(dto.getEmail());
        checkDuplicateMemberNickname(dto.getNickname());

        Member member = Member.builder()
                .nickname(dto.getNickname())
                .userId(dto.getUserId())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .telephone(dto.getTelephone())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(member);
        saveKeyword(member, dto.getKeywords());
        return member;
    }

    @Transactional
    public Member saveArtist(ArtistSaveRequestDto dto, MultipartFile image) throws IOException {

        checkDuplicateMemberUserID(dto.getUserId());
        checkDuplicateMemberEmail(dto.getEmail());
        checkDuplicateMemberNickname(dto.getNickname());

        String uuid = UUID.randomUUID().toString();
        String imageUrl = "";

        if(!image.isEmpty()){
            String ext = fileService.extractExt(image.getOriginalFilename());
            imageUrl = uuid + "." + ext;

            awsService.uploadImage(image, uuid);
        }

        Member member = Member.builder()
                .nickname(dto.getNickname())
                .userId(dto.getUserId())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .telephone(dto.getTelephone())
                .roles(Collections.singletonList("ROLE_ARTIST"))
                .education(dto.getEducation())
                .history(dto.getHistory())
                .description(dto.getDescription())
                .instagram(dto.getInstagram())
                .behance(dto.getBehance())
                .image(imageUrl)
                .build();

        memberRepository.save(member);
        return member;

    }

    @Transactional
    public MemberLoginResponseDto login(MemberLoginRequestDto dto) {

        // 이메일 및 비밀번호 유효성 체크
        Member findMember = memberRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ID));
        if (!passwordEncoder.matches(dto.getPassword(), findMember.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        TokenDto tokenDto = jwtTokenProvider.createToken(findMember.getUserId(), findMember.getRoles());
        jwtService.saveRefreshToken(tokenDto);

        MemberLoginResponseDto memberLoginResponseDto = MemberLoginResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .roles(findMember.getRoles().get(0))
                .build();

        return memberLoginResponseDto;
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));

        return findMember;
    }

    @Transactional
    public String resetPassword(String email) {
        final int PASSWORD_LENGTH = 8;

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));

        String password = UUID.randomUUID().toString().substring(0, PASSWORD_LENGTH);
        findMember.changePassword(passwordEncoder.encode(password));

        return password;
    }

    @Transactional
    public void changePassword(Long loginMemberId, String password) {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        password = passwordEncoder.encode(password);
        if (passwordEncoder.matches(password, findMember.getPassword())) {
            throw new CustomException(ErrorCode.SHOULD_CHANGE_PASSWORD);
        }

        findMember.changePassword(password);
    }

    @Transactional
    public void logout(String accessToken) {
        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        redisTemplate.opsForValue()
                .set(accessToken, "blackList", expiration, TimeUnit.MILLISECONDS);
    }

    @Transactional(readOnly = true)
    public void checkDuplicateMemberUserID(String userId) {
        if (memberRepository.existsByUserId(userId)) {
            throw new CustomException(ErrorCode.EXIST_USER_ID);
        }
    }

    @Transactional(readOnly = true)
    public void checkDuplicateMemberEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EXIST_USER_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public void checkDuplicateMemberNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.EXIST_USER_NICKNAME);
        }
    }

    public void saveKeyword(Member member, List<String> keywordList) {

        for (String keywordName : keywordList) {

            KeywordMap.checkExistsKeyword(keywordName);

            MemberKeyword memberKeyword = MemberKeyword.builder()
                    .member(member)
                    .keywordId(KeywordMap.map.get(keywordName))
                    .build();

            memberKeywordRepository.save(memberKeyword);
        }
    }

    @Transactional
    public void updateUser(Long loginMemberId, MemberUpdateRequest dto, MultipartFile image) throws IOException {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        String uuid = UUID.randomUUID().toString();
        String imageUrl = "";

        if(!image.isEmpty()){
            String ext = fileService.extractExt(image.getOriginalFilename());
            imageUrl = uuid + "." + ext;

            awsService.uploadImage(image, uuid);
        }

        if (findMember.isUpdatedEmail(dto.getEmail())) {
            checkDuplicateMemberEmail(dto.getEmail());
        }
        if (findMember.isUpdatedNickname(dto.getNickname())) {
            checkDuplicateMemberNickname(dto.getNickname());
        }

        findMember.updateUser(dto, imageUrl);
    }

    @Transactional
    public void updateArtist(Long loginMemberId, ArtistUpdateRequest dto, MultipartFile image) throws IOException {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        String uuid = UUID.randomUUID().toString();
        String imageUrl = "";

        if(!image.isEmpty()){
            String ext = fileService.extractExt(image.getOriginalFilename());
            imageUrl = uuid + "." + ext;

            awsService.uploadImage(image, uuid);
        }

        if(findMember.isUpdatedEmail(dto.getEmail())) {
            checkDuplicateMemberEmail(dto.getEmail());
        }
        if (findMember.isUpdatedNickname(dto.getNickname())) {
            checkDuplicateMemberNickname(dto.getNickname());
        }

        findMember.updateArtist(dto, imageUrl);
    }

    @Transactional
    public String changeRoles(Long memberId) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        findMember.changeRoles();

        return findMember.getRoles().get(0);
    }

    @Transactional
    public void withdrawUser(Long loginMemberId) {

        memberRepository.deleteById(loginMemberId);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(Long loginMemberId) {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 이미지 처리
        String imageUrl = awsStorageUrl + findMember.getImage();
        
        if(findMember.isBlankImage()) {
            imageUrl = null;
        }

        //키워드 처리
        List<String> keywordNameList = getKeywordName(findMember.getId());

        if(Objects.equals(findMember.getRoles().get(0), "ROLE_ARTIST")) {
            MemberResponse artistResponse = ArtistResponse.builder()
                    .nickname(findMember.getNickname())
                    .userId(findMember.getUserId())
                    .email(findMember.getEmail())
                    .telephone(findMember.getTelephone())
                    .image(imageUrl)
                    .keywords(keywordNameList)
                    .education(findMember.getEducation())
                    .history(findMember.getHistory())
                    .description(findMember.getDescription())
                    .instagram(findMember.getInstagram())
                    .behance(findMember.getBehance())
                    .build();

            return artistResponse;
        }

        MemberResponse memberResponse = MemberResponse.builder()
                .nickname(findMember.getNickname())
                .userId(findMember.getUserId())
                .email(findMember.getEmail())
                .telephone(findMember.getTelephone())
                .image(imageUrl)
                .keywords(keywordNameList)
                .build();

        return memberResponse;
    }

    @Transactional(readOnly = true)
    public List<String> getKeywordName(Long memberId) {

        List<MemberKeyword> findMemberKeywordList = memberKeywordRepository.findByMemberId(memberId);

        // keywordId(value) 리스트 구하기
        List<Integer> keywordIdList = new ArrayList();
        for(MemberKeyword memberKeyword : findMemberKeywordList){
            keywordIdList.add(memberKeyword.getKeywordId());
        }

        // keywordName(key) 리스트 구하기
        List<String> keywordNameList = new ArrayList();
        for(Integer keywordId : keywordIdList) {
            keywordNameList.add(KeywordMap.getKeywordName(keywordId));
        }

        return keywordNameList;
    }

    @Transactional
    public void pickArtist(Long loginMemberId, Long artistId) {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 작가id 유효성 검사
        Member findArtist = memberRepository.findById(artistId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTIST));
        if (!Objects.equals(findArtist.getRoles().get(0), "ROLE_ARTIST")) {
            throw new CustomException(ErrorCode.NOT_FOUND_ARTIST);
        }

        updatePreferredArtist(findMember, findArtist);
    }

    @Transactional
    public void updatePreferredArtist(Member member,Member artist) {

        // Column unique 제약조건 핸들링 (중복 컬럼 검증)
        if (memberPreferredArtistRepository.existsByMemberAndArtist(member, artist)){
            throw new CustomException(ErrorCode.EXIST_USER_PREFERRED_ARTIST);
        }

        MemberPreferredArtist memberPreferredArtist = MemberPreferredArtist.builder()
                .member(member)
                .artist(artist)
                .build();

        memberPreferredArtistRepository.save(memberPreferredArtist);
    }

    @Transactional
    public void pickArtWork(Long loginMemberId, Long artWorkId) {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        ArtWork findArtWork = artWorkRepository.findById(artWorkId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        updatePreferredArtWork(findMember, findArtWork);
    }

    private void updatePreferredArtWork(Member member, ArtWork artWork) {

        if (memberPreferredArtWorkRepository.existsByMemberAndArtWork(member, artWork)) {
            throw new CustomException(ErrorCode.EXIST_USER_PREFERRED_ARTWORK);
        }

        MemberPreferredArtWork memberPreferredArtWork = MemberPreferredArtWork.builder()
                .member(member)
                .artWork(artWork)
                .build();

        memberPreferredArtWorkRepository.save(memberPreferredArtWork);
    }

    @Transactional
    public void deletePreferredArtWork(Long loginMemberId, Long artWorkId) {

        Member findMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        ArtWork findArtWork = artWorkRepository.findById(artWorkId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARTWORK));

        memberPreferredArtWorkRepository.deleteByMemberAndArtWork(findMember, findArtWork);
    }
}