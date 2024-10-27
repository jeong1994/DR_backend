package com.dr.service.myPage;

import com.dr.dto.myPage.PointDetailDTO;
import com.dr.dto.myPage.UserInfoDTO;
import com.dr.mapper.myPage.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    // myPageMapper 의존성 주입
    @Autowired
    private final MyPageMapper myPageMapper;

    // userNumber를 사용하여 내 정보 정보 조회!
    public UserInfoDTO getUserInfo(Long userNumber) {
        return myPageMapper.getUserInfo(userNumber);

    }

    // userNumber를 사용하여 회원 탈퇴하기!
    public void deleteUser(Long userNumber) {
        myPageMapper.deleteUser(userNumber);
    }

    // userNumber를 사용하여 내 정보 포인트내역 확인!
    public List<PointDetailDTO> pointHistory(Long userNumber) {
        return myPageMapper.pointHistory(userNumber);

    }
}