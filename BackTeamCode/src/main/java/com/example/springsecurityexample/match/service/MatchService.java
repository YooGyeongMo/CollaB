package com.example.springsecurityexample.match.service;

import com.example.springsecurityexample.match.Match;
import com.example.springsecurityexample.match.Error;
import com.example.springsecurityexample.match.MatchRequestType;
import com.example.springsecurityexample.match.MatchSuccessType;
import com.example.springsecurityexample.match.dto.*;
import com.example.springsecurityexample.match.repository.MatchRepository;
import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.repository.MemberRepository;
import com.example.springsecurityexample.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor //DI
public class MatchService {
    public static final Object lock = new Object();
    private final MatchRepository matchRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    public Match RecommendUserWithRetry(MatchRequestDto matchRequestDto, int maxRetries, long retryDelayMillis) {
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                return RecommendUser(matchRequestDto);
            } catch (Exception e) {
                // 예외가 발생한 경우 재시도
                logger.error("매칭 실패", e);
                try {
                    Thread.sleep(retryDelayMillis);
                } catch (InterruptedException ex) {
                    logger.error("인터럽트", ex);
                    //인터럽트 상태면 인터럽트 설정 후 강제 종료
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            retryCount++;
        }

        throw new RuntimeException("매칭 상대 없거나, db에러 발생");
    }
    public Match RecommendUser(MatchRequestDto matchRequestDto) {

        //사용자의 프로필 카드를 Optional로 가져오기

        Optional<Profile> userInfo = profileRepository.findById(matchRequestDto.getUid1());


        //앱 사용자의 프로필 카드 가져오기
        Profile userProfile;
        if (userInfo.isPresent()) {
            userProfile = userInfo.get();
        } else {
            return null;
        }

        // 범위 내 주변 프로필 검색
        int userPeer = userInfo.get().getPeer();
        List<Profile> profilesInRange = profileRepository.findByPeerBetween(userPeer - 5, userPeer + 5);

        // 범위 내 프로필 카드가 없는 경우
        if (profilesInRange.isEmpty()) {
            return null;
        }



        synchronized(lock) {
            // 이미 매칭된 프로필 및 앱 사용자의 프로필 필터링용 배열
            List<Profile> filteredProfiles = new ArrayList<>();

            // 자기 자신이 아니며, 매칭이 안 만들어진 경우 추천 리스트로 넣음
            for (Profile profile : profilesInRange) {
                //여기서 반환 2개 에러 발생 가능
                boolean isAlreadyMatched = matchRepository.findByUid1AndProfile_Id(matchRequestDto.getUid1(), profile.getId()).isPresent();


                //존재 안 하면 ㄱ and 같지 않으면 ㄱ
                if (!isAlreadyMatched && !Objects.equals(profile, userProfile)) {
                    filteredProfiles.add(profile);
                }
            }
            // 필터링 후 남은 프로필 카드가 없으면 매칭 실패
            if (filteredProfiles.isEmpty()) {
                return null;
            }

            Profile recommanedProfile = filteredProfiles.get((int) (Math.random() * filteredProfiles.size()));
            // 매칭 정보 삽입
            boolean matchingReady = matchRepository.findByUid1AndProfile(matchRequestDto.getUid1(), recommanedProfile).isEmpty();
            if (matchingReady) {
                try{
                    return matchRepository.save(
                            Match.builder()
                                    .uid1(matchRequestDto.getUid1())
                                    .matchingDate(LocalDateTime.now())
                                    .matchSuccess(MatchSuccessType.PENDING)
                                    .matchingRequest(MatchRequestType.PENDING)
                                    .profile(recommanedProfile)
                                    .build());
                }catch (Exception e) {return null;}
            }
            else return null;
        }
    }

    //매칭 조회 (return type : Match)
    public List<Match> GetMatchingList (Long id){
        List<Match> matchList = matchRepository.findAllByUid1(id);
        //TODO : null exception 예외처리

        for (int i = 0; i < matchList.size(); i++) {
            matchList.get(i).update();
            if ( false
//                    원하는 조건을 붙혀서 원하는 매치 정보만 볼 수 있다.
//                    ex) matchList.get(i).getMatchingRequest() == MatchRequestType.REJECTED

            )
            {
                matchList.remove(i);
                i--;
            }
        }
        return matchList;
    }

    public Match GetMatch (Long id){
        Optional<Match> matchUser = matchRepository.findById(id);
        if (matchUser.isEmpty()) {
            return null;
        } else {
            return matchUser.get();
        }
    }

    //매칭 조회 (return type : profile)
    public List<Profile> GetMatchingUserProfileList (Long id){
        List<Match> matchUsers = GetMatchingList(id);
        if (matchUsers.isEmpty()) { //TODO:서비스에서 처리해야 의미있는 코드가 됨.
            return null;
        }

        List<Profile> profiles = new ArrayList<>();
        for (Match match : matchUsers) {
            Optional<Profile> optionalProfile = profileRepository.findById(match.getProfile().getId());
            if (optionalProfile.isPresent()){
                Profile profile = optionalProfile.get();
                profiles.add(profile);
            }
        }
        return profiles;
    }

    //매칭 삭제
    public Error DeleteMatchById (Long id){
        try {
            matchRepository.deleteById(id);
            return new Error(true, "success");
            //TODO: 좀 더 멀쩡해보이는 빌더 패턴으로 변경(후 순위)
        } catch (EntityNotFoundException ex) {
            return new Error(false, "Resource not found");
        }
    }

    public void DeleteAllMatches() {
        matchRepository.deleteAll();
    }

    //매칭 스케줄러용 함수
    public void DeleteRejectedMatches() {
        try {
            matchRepository.deleteByMatchingRequest(MatchRequestType.REJECTED);
        } catch (Exception e) {
            // 거절된 매칭 삭제
            logger.error("만들어진 매칭이 한 개도 없습니다. (10분 주기로 실행)", e);
        }
    }

    //매칭 스케줄러용 함수
    public void DeleteFalseMatches() {
        try {
            matchRepository.deleteByMatchSuccess(MatchSuccessType.FALSE);
        } catch (Exception e) {
            // 거절된 매칭 삭제
            logger.error("만들어진 매칭이 한 개도 없습니다. (10분 주기로 실행)", e);
        }
    }

    //매칭 성사
    public Match UpdateMatchStatusIsSuccess(Long uid1, Long uid2){
        Optional<Match> matchOptional = matchRepository.findByUid1AndProfile_Id(uid1, uid2);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setMatchSuccess(MatchSuccessType.SUCCESS);
            matchRepository.save(match);
            return match; // 변경된 Match 객체를 반환
        }
        return null;
    }

    //매칭 실패
    public Match UpdateMatchStatusIsFalse(Long uid1, Long uid2){
        Optional<Match> matchOptional = matchRepository.findByUid1AndProfile_Id(uid1, uid2);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setMatchSuccess(MatchSuccessType.FALSE);
            matchRepository.save(match);
            return match; // 변경된 Match 객체를 반환
        }
        return null;
    }

    //매칭 요청
    public Match LikeMatchStatusById (Long id){
        Optional<Match> matchOptional = matchRepository.findById(id);

        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setMatchingRequest(MatchRequestType.REQUESTED);
            matchRepository.save(match);
            return match; // 변경된 Match 객체를 반환
        }
        return null;
    }

    //매칭 거절
    public Match DislikeMatchStatusById (Long id){
        Optional<Match> matchOptional = matchRepository.findById(id);

        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setMatchingRequest(MatchRequestType.REJECTED);
            matchRepository.save(match);
            return match; // 변경된 Match 객체를 반환
        }
        return null;
    }

}