package com.dr.controller.chatBot;

import com.dr.dto.shop.PointShopDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatBot") //최상위 경로
public class NangjangbotController {

    @PostMapping("/nangjangbot") //하위경로
    public String openNangjangbot() {
        //구현
        return "chatBot/nangjangbot";
    }
}