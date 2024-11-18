package com.dr.service.rank;

import com.dr.dto.rank.RankDTO;
import com.dr.dto.myPage.PointCheckDTO;
import com.dr.mapper.rank.RankMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Component
public class RankService {

    // RankMapper 의존성 주입
    @Autowired
    private RankMapper rankMapper;

    // 특정 사용자의 랭킹 리스트를 가져오는 메서드
    public List<RankDTO> getRankList(Long userNumber) {
        return rankMapper.getRankList(userNumber);
    }

    // 50위까지의 랭킹 데이터를 가져오는 메서드
    public List<RankDTO> getFiftyRankList() {
        return rankMapper.fiftyRankList();
    }

//     월초 1일 00:00 1등 ~ 5등까지 사용자에게 포인트를 적립하는 메서드
//     @Scheduled(cron = "*/10 * * * * ?") // 10초마다 값이 들어가는지 확인(테스트용)
    @Scheduled(cron = "0 0 0 1 * ? ", zone = "Asia/Seoul")
    public void rewardTop5Users() {

        // 1등부터 5등까지 사용자 조회
        List<RankDTO> top5Users = rankMapper.Top5Rank();

        // 5등까지 포인트 지급
        for (RankDTO user : top5Users) {
            // 포인트 지급을 위한 DTO 생성
            PointCheckDTO pointDTO = new PointCheckDTO();
            pointDTO.setPointGet(500);
            pointDTO.setPointNote("랭킹");
            pointDTO.setUserNumber(user.getUserNumber());

            // 포인트 지급
            rankMapper.insertPoint(pointDTO);
        }
    }

    // 월초 1일 00:00 6등 ~ 10등까지 사용자에게 포인트를 적립하는 메서드
    // @Scheduled(cron = "*/10 * * * * ?") // 10초마다 값이 들어가는지 확인(테스트용)
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void rewardTop10Users() {
        // 6등부터 10등까지 사용자 조회
        List<RankDTO> top10Users = rankMapper.top10Rank();

        // 6등부터 10등까지 포인트 지급
        for (RankDTO user : top10Users) {
            // 포인트 지급을 위한 DTO 생성
            PointCheckDTO pointDTO = new PointCheckDTO();
            pointDTO.setPointGet(400);  // 예시로 400포인트 지급
            pointDTO.setPointNote("랭킹");
            pointDTO.setUserNumber(user.getUserNumber());

            // 포인트 지급
            rankMapper.insertPoint(pointDTO);
        }
    }

//     월초 1일 00:00 11등 ~ 20등까지 사용자에게 포인트를 적립하는 메서드
//     @Scheduled(cron = "*/10 * * * * ?") // 10초마다 값이 들어가는지 확인(테스트용)
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void rewardTop20Users() {
        // 11등부터 20등까지 사용자 조회
        List<RankDTO> top20Users = rankMapper.top20Rank();

        // 11등부터 20등까지 포인트 지급
        for (RankDTO user : top20Users) {
            // 포인트 지급을 위한 DTO 생성
            PointCheckDTO pointDTO = new PointCheckDTO();
            pointDTO.setPointGet(300);  // 300포인트 지급
            pointDTO.setPointNote("랭킹");
            pointDTO.setUserNumber(user.getUserNumber());

            // 포인트 지급
            rankMapper.insertPoint(pointDTO);
        }
    }

    // 월초 1일 00:00 21등 ~ 30등까지 사용자에게 포인트를 적립하는 메서드
    // @Scheduled(cron = "*/10 * * * * ?") // 10초마다 값이 들어가는지 확인(테스트용)
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void rewardTop30Users() {
        // 21등부터 30등까지 사용자 조회
        List<RankDTO> top30Users = rankMapper.top30Rank();

        // 21등부터 30등까지 포인트 지급
        for (RankDTO user : top30Users) {
            // 포인트 지급을 위한 DTO 생성
            PointCheckDTO pointDTO = new PointCheckDTO();
            pointDTO.setPointGet(200);  // 300포인트 지급
            pointDTO.setPointNote("랭킹");
            pointDTO.setUserNumber(user.getUserNumber());

            // 포인트 지급
            rankMapper.insertPoint(pointDTO);
        }
    }

    // 월초 1일 00:00 31등 ~ 50등까지 사용자에게 포인트를 적립하는 메서드
//     @Scheduled(cron = "*/10 * * * * ?") // 10초마다 값이 들어가는지 확인(테스트용)
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void rewardTop50Users() {
        // 31등부터 50등까지 사용자 조회
        List<RankDTO> top50Users = rankMapper.top50Rank();

        // 6등부터 10등까지 포인트 지급
        for (RankDTO user : top50Users) {
            // 포인트 지급을 위한 DTO 생성
            PointCheckDTO pointDTO = new PointCheckDTO();
            pointDTO.setPointGet(100);  // 300포인트 지급
            pointDTO.setPointNote("랭킹");
            pointDTO.setUserNumber(user.getUserNumber());

            // 포인트 지급
            rankMapper.insertPoint(pointDTO);
        }
    }

    // 월초 1일 00:00 그외 등수  사용자에게 포인트를 적립하는 메서드
//     @Scheduled(cron = "*/10 * * * * ?") // 10초마다 값이 들어가는지 확인(테스트용)
    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Seoul")
    public void rewardTopOtherUsers() {
        // 그외 등수 사용자 조회
        List<RankDTO> topOtherUsers = rankMapper.topOtherRank();

        // 그외 등수 포인트 지급
        for (RankDTO user : topOtherUsers) {
            // 포인트 지급을 위한 DTO 생성
            PointCheckDTO pointDTO = new PointCheckDTO();
            pointDTO.setPointGet(50);  // 300포인트 지급
            pointDTO.setPointNote("랭킹");
            pointDTO.setUserNumber(user.getUserNumber());

            // 포인트 지급
            rankMapper.insertPoint(pointDTO);
        }
    }
}
