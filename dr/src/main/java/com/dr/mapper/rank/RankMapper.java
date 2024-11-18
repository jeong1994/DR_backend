package com.dr.mapper.rank;

import com.dr.dto.myPage.PointCheckDTO;
import com.dr.dto.rank.RankDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.management.relation.RelationNotification;
import java.util.List;

@Mapper
public interface RankMapper {
    // 특정 사용자의 랭킹 데이터를 가져오는 메서드
    List<RankDTO> getRankList(@Param("userNumber") Long userNumber);

    // 50위까지의 랭킹 데이터를 가져오는 메서드
    List<RankDTO> fiftyRankList();

    // 1등부터 5등까지 랭킹 데이터를 가져오는 메서드
    List<RankDTO> Top5Rank();

    // 6등부터 10등까지 랭킹 데이터를 가져오는 메서드
    List<RankDTO> top10Rank();

    // 11등부터 20등까지 랭킹 데이터를 가져오는 메서드
    List<RankDTO> top20Rank();

    // 21등부터 30등까지 랭킹 데이터를 가져오는 메서드
    List<RankDTO> top30Rank();

    // 31등부터 50등까지 랭킹 데이터를 가져오는 메서드
    List<RankDTO> top50Rank();

    // 그외 등수 랭킹 데이터를 가져오는 메서드
    List<RankDTO> topOtherRank();


    // 월초 1등부터 5등까지의 사용자에게 포인트를 적립하는 메서드
    void insertPoint(PointCheckDTO pointCheckDTO);




}