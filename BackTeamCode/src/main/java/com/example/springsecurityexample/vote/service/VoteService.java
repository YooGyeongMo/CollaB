package com.example.springsecurityexample.vote.service;


import com.example.springsecurityexample.Chat.ChatDTO;
import com.example.springsecurityexample.Chat.ChatRoom;
import com.example.springsecurityexample.vote.Vote;
import com.example.springsecurityexample.vote.dto.VoteDTO;
import com.example.springsecurityexample.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {
// TODO createVote에 총 투표인원까지 build해서 파싱

    private final VoteRepository voteRepository;
    private final JdbcTemplate jdbcTemplate;

    public List<Vote> getAllVotes(String roomId) {
        return voteRepository.findVotesByRoomId(roomId);
    }

    public VoteDTO createVote(VoteDTO voteDTO) {
        voteDTO.setStartDate(LocalDate.now());
        Vote vote = Vote.builder()
                .voteTitle(voteDTO.getVoteTitle())
                .endDate(voteDTO.getEndDate())
                .roomId(voteDTO.getRoomId())
                .startDate(voteDTO.getStartDate())
                .voteStatus(true)
                .totalVoteNumber(countVoteParticipantsNumber(voteDTO.getRoomId()))
                .build();
        voteRepository.save(vote);
        return voteDTO;
    }

    public int countVoteParticipantsNumber(String roomId) {
        String insertUserQuery = "select count(*) as totalVoteNumber from chatroomparticipants where roomid = ?";
        Object[] param = {roomId};
        return jdbcTemplate.queryForObject(insertUserQuery, param, Integer.class);
    }

    public boolean endVote(int voteId) {
        Vote vote = voteRepository.findVotesByVoteId(voteId);
        vote.setVoteStatus(false);
        return vote.isVoteStatus();
    }
}
