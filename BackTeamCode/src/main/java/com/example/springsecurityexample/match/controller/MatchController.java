package com.example.springsecurityexample.match.controller;

import com.example.springsecurityexample.match.*;
import com.example.springsecurityexample.match.Error;
import com.example.springsecurityexample.match.dto.*;
import com.example.springsecurityexample.match.service.MatchService;
import com.example.springsecurityexample.member.Member;
import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.controller.ProfileController;
import com.example.springsecurityexample.security.CustomUserDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor //DI
@RequestMapping(value = "/api", produces = MediaTypes.HAL_JSON_VALUE)
public class MatchController {

    private final MatchService matchService;

    // 사용 중인 user Id 찾아오기.
//    Long GetLoginUserId () {
//        Long id;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            Member member = userDetails.getMember();
//            id = member.getId();
//            return id;
//        }
//        else return null;
//    }

    @ApiOperation(
            value = "매칭 생성"
            , notes = "json을 통해 받은 사용자의 정보를 통해 매칭을 생성한다.\n")
    @PostMapping("/post/match")
    public ResponseEntity<MatchResource> CreateMatch(@RequestBody MatchRequestDto matchRequestDto) {
        //TODO : 에러 관련 코드 추가(하는 중)
        //TODO : 필터링 기능 (후 순위)

        //요청 body가 비어있는 경우
        if (matchRequestDto.getUid1() == null) { //uid1 body에 담아서 요청했는지 확인
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        //다른 유저의 id로 매칭을 만드려고 하는 경우
//        if (!Objects.equals(matchRequestDto.getUid1(), GetLoginUserId())) {
//            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//        }


        //매칭 생성
        Match match = matchService.RecommendUserWithRetry(matchRequestDto, 5, 1000);
        //Match matchv1 = matchService.RecommendUser(matchRequestDto);

        //매치 생성 실패
        if (match == null || match.getMatchId() == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        // @@@@@@@@@@ 동기화 도전

        match.update();

        // @@@@@@@@@@ 동기화 도전

        //링크 추가
        var selfLinkBuilder = linkTo(methodOn(MatchController.class).CreateMatch(matchRequestDto));
        URI createdUri = selfLinkBuilder.toUri();
        var getUserMatchesLinkBuilder = linkTo(methodOn(MatchController.class).ReadMatch(match.getUid1()));
        var getUserMatchesByProfileLinkBuilder = linkTo(methodOn(MatchController.class).ReadMatchingPartnerProfile(match.getUid1()));
        var updateDeleteLinkBuilder = linkTo(methodOn(MatchController.class).DeleteMatch(match.getMatchId()));
        var updateLikeLinkBuilder = linkTo(methodOn(MatchController.class).LikeMatch(match.getMatchId()));
        var updateDisLikeLinkBuilder = linkTo(methodOn(MatchController.class).DislikeMatch(match.getMatchId()));

        MatchResource matchResource = new MatchResource(match);
        matchResource.add(selfLinkBuilder.withSelfRel());
        //matchResource.add(getUserMatchesLinkBuilder.withRel("Read match list(매칭 조회 - 반환형 : match list, get)"));
        //matchResource.add(getUserMatchesByProfileLinkBuilder.withRel("Read matching partner profile card list(매칭 상대방 조회 - 반환형 : profile list, get)"));
        //matchResource.add(updateDeleteLinkBuilder.withRel("Delete match(매칭 삭제, delete)"));
        matchResource.add(updateLikeLinkBuilder.withRel("Request match(매칭 요청, patch)"));
        matchResource.add(updateDisLikeLinkBuilder.withRel("Reject match(매칭 거절, patch)"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(createdUri)
                .body(matchResource);
    }

    @ApiOperation(
            value = "알람 기능에서 쓰면 좋을것 같아요. 매칭 id를 통한 매칭 조회(응답형식 : Match 엔티티)"
            , notes = "알람 기능에서 쓸 수 있을까 싶어서 만들었어요.")
    @GetMapping("/get/match-info/match/{matchId}")
    public ResponseEntity<MatchResource> ReadMatchByMatchId(@PathVariable Long matchId) {
        Match match = matchService.GetMatch(matchId);

        //링크 생성
        var selfLinkBuilder = linkTo(methodOn(MatchController.class).ReadMatchByMatchId(matchId));
        URI createdUri = selfLinkBuilder.toUri();
        var FalseMatchLinkBuilder = linkTo(methodOn(MatchController.class).FalseMatch(match.getUid1(), match.getProfile().getId()));
        var SuccessMatchLinkBuilder = linkTo(methodOn(MatchController.class).SuccessMatch(match.getUid1(), match.getProfile().getId()));
        var GetRequesterInformation = linkTo(methodOn(ProfileController.class).getProfileByMemberId(matchId));

        MatchResource matchResource = new MatchResource(match);
        matchResource.add(selfLinkBuilder.withSelfRel());
        matchResource.add(GetRequesterInformation.withRel("get requester's information(매칭 요청자 정보, get)"));
        matchResource.add(FalseMatchLinkBuilder.withRel("false match(매칭 실패, patch)"));
        matchResource.add(SuccessMatchLinkBuilder.withRel("success match(매칭 성공, patch)"));

        return ResponseEntity
                .status(match == null ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .location(createdUri)
                .body(matchResource);
    }

    @ApiOperation(
            value = "사용자 매칭 조회(응답형식 : List<프로필>)"
            , notes = "URI를 통해 받은 사용자의 ID로 사용자의 매칭상대의 프로필 리스트를 조회한다.")
    @GetMapping("/get/match-info/profile/{uid1}")
    public ResponseEntity<List<Profile>> ReadMatchingPartnerProfile(@PathVariable Long uid1) {
        List<Profile> profiles = matchService.GetMatchingUserProfileList(uid1);
        return ResponseEntity
                .status(profiles.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .body(profiles.isEmpty() ? null : profiles);
    }

    @ApiOperation(
            value = "사용자 매칭 조회(응답형식 : List<매치>)"
            , notes = "URI를 통해 받은 사용자의 ID로 사용자의 매칭 리스트를 조회한다.")
    @GetMapping("/get/match-info/match-list/{uid1}")
    public ResponseEntity<List<Match>> ReadMatch(@PathVariable Long uid1) { //계정 매개변수 있어야하나?
        List<Match> Matches = matchService.GetMatchingList(uid1);
        return ResponseEntity
                .status(Matches.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .body(Matches.isEmpty() ? null : Matches);
    }

    @ApiOperation(
            value = "매칭 삭제"
            , notes = "URI를 통해 받은 매칭 ID로 매칭을 삭제한다. " +
            "\n매칭 삭제는 사용할 일 없을것 같습니다")
    @DeleteMapping("/delete/match/{matchId}")
    public ResponseEntity<Error> DeleteMatch(@PathVariable Long matchId){
        Error error = matchService.DeleteMatchById(matchId);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ApiOperation(
            value = "매칭 성사(매칭 요청을 알람에서 수락)"
            , notes = "URI를 통해 받은 매칭 ID로 매칭 성사 여부를 저장하는 칼럼을 SUCCESS로 변경" +
            "\n매칭 성사 시 사용.")
    @PatchMapping("/patch/match/success/{sender}/{receiver}")
    public ResponseEntity<Match> SuccessMatch(@PathVariable Long sender, @PathVariable Long receiver){
        Match match = matchService.UpdateMatchStatusIsSuccess(sender ,receiver);
        return ResponseEntity
                .status(
                        match != null
                                ? HttpStatus.OK
                                : HttpStatus.NO_CONTENT
                )
                .body(
                        match
                );
    }

    @ApiOperation(
            value = "매칭 실패(매칭 요청을 알람에서 거절)"
            , notes = "URI를 통해 받은 매칭 ID로 매칭 성사 여부를 저장하는 칼럼을 FALSE로 변경" +
            "\n매칭 실패 시 사용.")
    @PatchMapping("/patch/match/false/{sender}/{receiver}")
    public ResponseEntity<Match> FalseMatch(@PathVariable Long sender, @PathVariable Long receiver){
        Match match = matchService.UpdateMatchStatusIsFalse(sender ,receiver);
        return ResponseEntity
                .status(
                        match != null
                                ? HttpStatus.OK
                                : HttpStatus.NO_CONTENT
                )
                .body(
                        match
                );
    }

    @ApiOperation(
            value = "매칭 요청(스와이프에서 매칭을 수락하는 경우)"
            , notes = "URI를 통해 받은 매칭 ID로 매칭 요청 여부를 저장하는 칼럼을 REQUESTED로 변경" +
            "\n매칭 요청 시 사용 권장.")
    @PatchMapping("/patch/match/like/{matchId}")
    public ResponseEntity<Match> LikeMatch(@PathVariable Long matchId){
        Match match = matchService.LikeMatchStatusById(matchId);
        return ResponseEntity
                .status(
                        match != null
                                ? HttpStatus.OK
                                : HttpStatus.NO_CONTENT
                )
                .body(
                        match
                );
    }

    @ApiOperation(
            value = "매칭 거절(스와이프에서 매칭을 거절하는 경우)"
            , notes = "URI를 통해 받은 매칭 ID로 매칭 요청 여부를 저장하는 칼럼을 REJECTED로 변경" +
            "\n매칭 거절 시 사용 권장.")
    @PatchMapping("/patch/match/dislike/{matchId}")
    public ResponseEntity<Match> DislikeMatch(@PathVariable Long matchId){
        Match match = matchService.DislikeMatchStatusById(matchId);
        return ResponseEntity
                .status(
                        match != null
                                ? HttpStatus.OK
                                : HttpStatus.NO_CONTENT
                )
                .body(
                        match
                );
    }
}