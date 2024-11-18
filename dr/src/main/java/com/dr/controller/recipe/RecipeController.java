package com.dr.controller.recipe;


import com.dr.dto.recipe.*;
import com.dr.service.recipe.RecipeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.CtBehavior;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    //    나만의레시피최신순
    @GetMapping("/myRecipeList")
    public String MyRecipeList(Model model) {
        // 전체 레시피 목록 조회
        List<MyRecipeListDTO> recipeList = recipeService.findAllRecipes();
        // 모델에 레시피 목록 추가
        model.addAttribute("recipeList", recipeList);
        return "recipe/myRecipeList";  // myRecipeList.html로 데이터 전달
    }

    //    나만의 레시피 추천순
    @GetMapping("/myRecipeListGood")
    public String myRecipeListGood(Model model) {
        List<MyRecipeListDTO> recipeListGood = recipeService.findAllRecipesGood();
        model.addAttribute("recipeList", recipeListGood);
        return "recipe/myRecipeList";  // myRecipeList.html로 데이터 전달
    }



    //    나만의레시피 상세페이지 + 댓글 조회
    @GetMapping("/myDetailPage")
    public String myDetailPage(@RequestParam("recipeNumber") Long recipeNumber,  @SessionAttribute(value = "userNickName", required = false) String userNickName ,  Model model) {
        // 특정 레시피의 상세 정보 조회
        MyRecipeDetailDTO recipeDetail = recipeService.findMyRecipeDetail(recipeNumber);

        // 특정 레시피의 댓글 목록 조회
        List<MyRecipeCommentDTO> recipeComments = recipeService.selectMyRecipeComment(recipeNumber);

        // 모델에 레시피 상세 정보 추가
        model.addAttribute("recipeDetail", recipeDetail);
        model.addAttribute("recipeComments", recipeComments);
        model.addAttribute("userNickName",userNickName);

        return "recipe/myDetailPage";  // myDetailPage.html로 데이터 전달
    }


    //    나만의 레시피 상세페이지 댓글작성
    @PostMapping("/myDetailPage")
    public String insertComment(@RequestParam("recipeNumber") Long recipeNumber,
                                @RequestParam("replyText") String replyText,
                                @RequestParam("userNumber") Long userNumber,
                                RedirectAttributes redirectAttributes) {
        if (recipeNumber == null) {
            throw new IllegalArgumentException("Recipe number is required.");
        }
        MyRecipeCommentDTO commentDTO = new MyRecipeCommentDTO();
        commentDTO.setRecipeNumber(recipeNumber);
        commentDTO.setReplyText(replyText);
        commentDTO.setUserNumber(userNumber);
        log.info(commentDTO.getReplyNumber() + "작성 확인====");
        log.info(commentDTO.getReplyText() + "작성 확인 === ");
        recipeService.insertMyRecipeComment(commentDTO);

        redirectAttributes.addAttribute("recipeNumber", recipeNumber);
        return "redirect:/recipe/myDetailPage";
    }

    //    나만의 레시피 댓글 수정
    @PostMapping("/updateMyReply")
    public ResponseEntity<Void> updateMyRecipeComment(@RequestParam(name="replyNumber", required = false) Long replyNumber, @RequestParam("replyText") String replyText){
        log.info("컨트롤러 확인============");
        if (replyNumber == null || replyText == null || replyText.trim().isEmpty()) {
            log.info(replyNumber +" replyNumber 확인====");
            log.info(replyText +" replyText 확인====");
            log.info(replyText.trim().isEmpty() +" replyNumber 확인====");
            return ResponseEntity.badRequest().build(); // 잘못된 요청 처리
        }

        // 댓글 수정 서비스 호출
        recipeService.updateMyRecipeComment(replyNumber, replyText);
        // 수정 완료 후 성공 응답 반환
        return ResponseEntity.ok().build();
    }


    //      나만의 레시피 상세페이지 댓글 삭제
    @PostMapping("/deleteMyReply")
    public ResponseEntity<Void> deleteMyRecipeComment(@RequestParam("replyNumber") Long replyNumber) {
        if (replyNumber == null) {
            return ResponseEntity.badRequest().build(); // 잘못된 요청 처리
        }
        // 댓글 삭제 서비스 호출
        log.info(replyNumber.toString() + "댓글삭제");
        recipeService.deleteMyRecipeComment(replyNumber);
        // 삭제 완료 후 성공 응답 반환
        return ResponseEntity.ok().build();
    }

    //    챗봇 레시피 최신순
    @GetMapping("/chatBotRecipeList")
    public String chatBotRecipeList(Model model) {
        // 전체 레시피 목록 조회
        List<ChatBotRecipeListDTO> chatBotRecipeList = recipeService.findAllRecipes1();
        // 모델에 레시피 목록 추가
        model.addAttribute("chatBotRecipeList", chatBotRecipeList);
        return "recipe/chatBotRecipeList";  // chatBotRecipeList.html로 데이터 전달
    }

    //    챗봇 레시피 추천순
    @GetMapping("/chatBotRecipeListGood")
    public String chatBotRecipeListGood(Model model) {
        List<ChatBotRecipeListDTO> chatBotRecipeListGood = recipeService.findAllRecipes1Good();
        model.addAttribute("chatBotRecipeList", chatBotRecipeListGood);
        return "recipe/chatBotRecipeList";
    }

    //    챗봇레시피 상세페이지 + 댓글조회
    @GetMapping("/chatBotDetailPage")
    public String ChatBotDetailPage(@RequestParam("recipeNumber") Long recipeNumber,  @SessionAttribute(value = "userNickName", required = false) String userNickName ,  Model model) {
        // 특정 레시피의 상세 정보 조회
        ChatBotRecipeDetailDTO chatBotRecipeDetail = recipeService.findChatBotRecipeDetail(recipeNumber);
        List<ChatBotRecipeCommentDTO> chatBotRecipeComments = recipeService.selectChatBotRecipeComment(recipeNumber);
        // 모델에 레시피 상세 정보 추가
        model.addAttribute("chatBotRecipeDetail", chatBotRecipeDetail);
        model.addAttribute("chatBotRecipeComments", chatBotRecipeComments);
        model.addAttribute("userNickName",userNickName);
        return "recipe/chatBotDetailPage";  // ChatBotDetailPage.html로 데이터 전달
    }

    // 챗봇 레시피 댓글 작성
    @PostMapping("/chatBotDetailPage")
    public String ChatBotinsertComment(@RequestParam("recipeNumber") Long recipeNumber,
                                       @RequestParam("replyText") String replyText,
                                       @RequestParam("userNumber") Long userNumber,
                                       RedirectAttributes redirectAttributes) {
        if (recipeNumber == null) {
            throw new IllegalArgumentException("Recipe number is required.");
        }

        ChatBotRecipeCommentDTO chatBotRecipeCommentDTO = new ChatBotRecipeCommentDTO();
        chatBotRecipeCommentDTO.setRecipeNumber(recipeNumber);
        chatBotRecipeCommentDTO.setReplyText(replyText);
        chatBotRecipeCommentDTO.setUserNumber(userNumber);
        recipeService.insertChatBotRecipeComment(chatBotRecipeCommentDTO);

        redirectAttributes.addAttribute("recipeNumber", recipeNumber);
        return "redirect:/recipe/chatBotDetailPage";
    }

    //    챗봇 레시피 댓글 수정
    @PostMapping("/updateChatBotReply")
    public ResponseEntity<Void> updateChatBotRecipeComment(@RequestParam(name="replyNumber", required = false) Long replyNumber, @RequestParam("replyText") String replyText){
        if (replyNumber == null || replyText == null || replyText.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 잘못된 요청 처리
        }
        // 댓글 수정 서비스 호출
        recipeService.updateChatBotRecipeComment(replyNumber, replyText);
        // 수정 완료 후 성공 응답 반환
        return ResponseEntity.ok().build();
    }

    // 챗봇 레시피 상세페이지 댓글 삭제
    @PostMapping("/deleteChatBotReply")
    public ResponseEntity<Void> deleteChatBotRecipeComment(@RequestParam("replyNumber") Long replyNumber) {
        if (replyNumber == null) {
            return ResponseEntity.badRequest().build(); // 잘못된 요청 처리
        }
        // 댓글 삭제 서비스 호출
        recipeService.deleteChatBotRecipeComment(replyNumber);
        // 삭제 완료 후 성공 응답 반환
        return ResponseEntity.ok().build();
    }

    //    나만의레시피 글쓰기 페이지로 이동
    @GetMapping("/myRecipeWriter")  // 레시피 작성 페이지로 이동
    public String recipeWriteForm(Model model) {
        model.addAttribute("myRecipeWriteDTO", new MyRecipeWriteDTO());
        // 빈 DTO 객체 생성 및 전달
        return "recipe/myRecipeWriter";  // myRecipeWriter.html로 데이터 전달
    }

    // 나만의레시피 글쓰기
    @PostMapping("/myRecipeWriterOk")
    public String submitRecipe(
            @RequestParam("recipeTitle") String recipeTitle,
            @RequestParam("recipeText") String recipeText,
            @RequestParam("photoOriginal") String photoOriginal,
            @RequestParam("photoLocal") String photoLocal,
            @RequestParam("photoSize") String photoSize,
            @SessionAttribute(value = "userNumber", required = false) Long userNumber) {
        // 1. MyRecipeWriteDTO 생성 (레시피 정보)
        MyRecipeWriteDTO myRecipeWriteDTO = new MyRecipeWriteDTO();
        myRecipeWriteDTO.setRecipeTitle(recipeTitle);
        myRecipeWriteDTO.setRecipeText(recipeText);
        myRecipeWriteDTO.setUserNumber(userNumber);
        myRecipeWriteDTO.setRecipeType("나만의레시피");

        // 2. RecipePhotoDTO 생성 (사진 정보)
        RecipePhotoDTO recipePhotoDTO = new RecipePhotoDTO();
        recipePhotoDTO.setPhotoOriginal(photoOriginal);
        recipePhotoDTO.setPhotoLocal(photoLocal);
        recipePhotoDTO.setPhotoSize(photoSize);

        // 3. RecipeService 호출하여 레시피와 사진 저장
        recipeService.saveRecipe(myRecipeWriteDTO, recipePhotoDTO);

        // 4. 성공 메시지 전달 후, 리디렉션
        return "redirect:/recipe/myRecipeList"; // 리디렉션 URL은 필요에 맞게 수정
    }

    //챗봇 글쓰기 페이지 이동
    @GetMapping("/chatBotRecipeWriter")  // 레시피 작성 페이지로 이동
    public String recipeChatBotWriteForm(Model model) {
        model.addAttribute("chatBotRecipeWriteDTO", new ChatBotRecipeWriteDTO());
        // 빈 DTO 객체 생성 및 전달
        log.info("여기는 GetMapping@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return "recipe/chatBotRecipeWriter";  // myRecipeWriter.html로 데이터 전달
    }

    // 챗봇레시피 글쓰기
    @PostMapping("/chatBotRecipeWriterOk")
    public String submitChatBotRecipe(
            @RequestParam("recipeTitle") String recipeTitle,
            @RequestParam("recipeText") String recipeText,
            @RequestParam("photoOriginal") String photoOriginal,
            @RequestParam("photoLocal") String photoLocal,
            @RequestParam("photoSize") String photoSize,
            @SessionAttribute(value = "userNumber", required = false) Long userNumber) {

        // 1. MyRecipeWriteDTO 생성 (레시피 정보)
        ChatBotRecipeWriteDTO chatBotRecipeWriteDTO = new ChatBotRecipeWriteDTO();
        chatBotRecipeWriteDTO.setRecipeTitle(recipeTitle);
        chatBotRecipeWriteDTO.setRecipeText(recipeText);
        chatBotRecipeWriteDTO.setUserNumber(userNumber);
        chatBotRecipeWriteDTO.setRecipeType("챗봇레시피");

        log.info(chatBotRecipeWriteDTO.toString()+"aslknlksdnfvklsdnklgnkldgsnkl;bsd");

        // 2. RecipePhotoDTO 생성 (사진 정보)
        RecipePhotoDTO recipePhotoDTO = new RecipePhotoDTO();
        recipePhotoDTO.setPhotoOriginal(photoOriginal);
        recipePhotoDTO.setPhotoLocal(photoLocal);
        recipePhotoDTO.setPhotoSize(photoSize);

        log.info(recipePhotoDTO.toString()+"asdlknldabndfklnlkldbba");

        // 3. RecipeService 호출하여 레시피와 사진 저장
        recipeService.saveChatBotRecipe(chatBotRecipeWriteDTO, recipePhotoDTO);

        // 4. 환경기여 점수 10점 추가
        ScoreCheckDTO scoreCheckDTO = new ScoreCheckDTO();
        scoreCheckDTO.setUserNumber(userNumber);  // 사용자 번호 설정
        scoreCheckDTO.setScoreGet(10L);           // 10점 설정
        recipeService.insertScoreByRecipe(scoreCheckDTO);  // 환경기여 점수 추가

        // 5. 성공 메시지 전달 후, 리디렉션
        return "redirect:/recipe/chatBotRecipeList"; // 리디렉션 URL은 필요에 맞게 수정
    }



    // 나만의 레시피 추천 수 증가
    @PostMapping("/goodPlus")
    public ResponseEntity<Void> addGood(@RequestBody MyRecipeGoodDTO myRecipeGoodDTO,
                                        @SessionAttribute(value = "userNumber",required = false) Long userNumber) {
        myRecipeGoodDTO.setUserNumber(userNumber);
        recipeService.addGood(myRecipeGoodDTO);

        // 4. 환경기여 점수 5점 추가
        ScoreCheckDTO scoreCheckDTO = new ScoreCheckDTO();
        scoreCheckDTO.setUserNumber(userNumber);  // 사용자 번호 설정
        scoreCheckDTO.setScoreGet(5L);           // 5점 설정

        recipeService.insertScorerecommand(scoreCheckDTO);  // 환경기여 점수 추가

        return new ResponseEntity<>(HttpStatus.OK);


    }

    // 나만의 레시피추천 수 감소
    @PostMapping("/goodMinus")
    public ResponseEntity<Void> removeGood(@RequestBody MyRecipeGoodDTO myRecipeGoodDTO,
                                           @SessionAttribute(value = "userNumber",required = false) Long userNumber) {
        myRecipeGoodDTO.setUserNumber(userNumber);
        recipeService.removeGood(myRecipeGoodDTO);

        // 점수 차감: 추천이 해제된 경우 -5점 차감
        ScoreCheckDTO scoreCheckDTO = new ScoreCheckDTO();
        scoreCheckDTO.setUserNumber(userNumber);  // 유저 번호 설정
        recipeService.deleteScorerecommand(scoreCheckDTO);  // 점수 차감 메서드 호출

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 챗봇 레시피 추천 수 증가
    @PostMapping("/ChatBotGoodPlus")
    public ResponseEntity<Void> ChatBotAddGood(@RequestBody ChatBotRecipeGoodDTO chatBotRecipeGoodDTO,
                                               @SessionAttribute(value = "userNumber",required = false) Long userNumber) {
        chatBotRecipeGoodDTO.setUserNumber(userNumber);
        recipeService.ChatBotAddGood(chatBotRecipeGoodDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 챗봇 레시피 추천 수 감소
    @PostMapping("/ChatBotgoodMinus")
    public ResponseEntity<Void> ChatBotRemoveGood(@RequestBody ChatBotRecipeGoodDTO chatBotRecipeGoodDTO,
                                                  @SessionAttribute(value = "userNumber",required = false) Long userNumber) {
        chatBotRecipeGoodDTO.setUserNumber(userNumber);
        recipeService.ChatBotRemoveGood(chatBotRecipeGoodDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //신고페이지 이동
    @GetMapping("/report")
    public String recipeReportPage(@RequestParam("recipeNumber") Long recipeNumber,
                                   @RequestParam(value = "replyNumber", required = false) Long replyNumber,
                                   Model model) {
        model.addAttribute("recipeNumber", recipeNumber);
        model.addAttribute("replyNumber", replyNumber);
        return "/recipe/report";
    }

    //신고 완료 후 페이지 이동
    @PostMapping("/reportOk")
    public String recipeReportOk(@RequestParam("recipeNumber") Long recipeNumber,
                                 @RequestParam(value = "replyNumber", required = false) Long replyNumber,
                                 @SessionAttribute(value = "userNumber", required = false) Long userNumber,
                                 @RequestParam("reason") String reason,
                                 @RequestParam(value = "otherReasonText", required = false) String otherReasonText) {

        ChatBotRecipeDetailDTO chatBotRecipeDetailDTO = recipeService.findChatBotRecipeDetail(recipeNumber);
        RecipeReportDTO recipeReportDTO = new RecipeReportDTO();

        // 1. 사유 지정
        if (otherReasonText != null && !otherReasonText.trim().isEmpty()) {
            recipeReportDTO.setSirenReason(otherReasonText);
        } else {
            recipeReportDTO.setSirenReason(reason);
        }

        // 2. sirenType 지정 및 게시판, 댓글 번호 지정
        if (replyNumber == null) {
            recipeReportDTO.setSirenType("레시피");
            recipeReportDTO.setRecipeNumber(recipeNumber);
        } else {
            recipeReportDTO.setSirenType("댓글");
            recipeReportDTO.setReplyNumber(replyNumber);
        }

        // 3. 유저 넘버 지정
        recipeReportDTO.setUserNumber(userNumber);

        // 신고 처리
        recipeService.report(recipeReportDTO);

        // 4. 리디렉션 처리
        if (chatBotRecipeDetailDTO != null && "챗봇레시피".equals(chatBotRecipeDetailDTO.getRecipeType())) {
            return "redirect:/recipe/chatBotDetailPage?recipeNumber=" + recipeNumber;
        } else {
            return "redirect:/recipe/myDetailPage?recipeNumber=" + recipeNumber;
        }
    }

    // 찜 추가 메서드 (좋아요)
    @PostMapping("/like")
    public ResponseEntity<Void> addLike(
            @SessionAttribute(value = "userNumber", required = false) Long userNumber, @RequestBody RecipeSteamDTO recipeSteamDTO) {
        try {
            recipeSteamDTO.setUserNumber(userNumber);
            recipeSteamDTO.setRecipeNumber(recipeSteamDTO.getRecipeNumber());
            // 실제 찜 추가 로직을 호출
            recipeService.addSteam(recipeSteamDTO);
            return ResponseEntity.ok().build();  // 성공 시 200 OK 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).build();  // 서버 오류 반환
        }
    }

    // 찜 삭제 메서드 (싫어요)
    @PostMapping("/unLike")
    public ResponseEntity<Void> removeLike(
            @SessionAttribute(value = "userNumber", required = false) Long userNumber, @RequestBody RecipeSteamDTO recipeSteamDTO) {
        try {
            recipeSteamDTO.setUserNumber(userNumber);
            recipeSteamDTO.setRecipeNumber(recipeSteamDTO.getRecipeNumber());

            // 실제 찜 삭제 로직을 호출
            recipeService.removeSteam(recipeSteamDTO);
            return ResponseEntity.ok().build();  // 성공 시 200 OK 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).build();  // 서버 오류 반환
        }
    }

    // 나만의 레시피 삭제
    @PostMapping("/deleteRecipe")
    public String deleteRecipe(@RequestParam("recipeNumber2") Long recipeNumber) {
        recipeService.deleteRecipeAndPhoto(recipeNumber);
        return "redirect:/recipe/myRecipeList";
    }

    // 챗봇 레시피 삭제
    @PostMapping("/deleteChatBotRecipe")
    public String updateChatBotRecipe(@RequestParam("recipeNumber5") Long recipeNumber) {
        recipeService.deleteChatBot(recipeNumber);
        return "redirect:/recipe/chatBotRecipeList";
    }

    //나만의 레시피 수정 이동
    @GetMapping("/updateRecipe")
    public String updateRecipe(@RequestParam("recipeNumber3") Long recipeNumber,
                               @RequestParam("recipeTitle") String recipeTitle,
                               @RequestParam("recipeText") String recipeText,
                               @RequestParam(value = "photoLocal", required = false) String photoLocal, // 사진 파일 경로 추가
                               @RequestParam(value = "photoOriginal", required = false) String photoOriginal,
                               @RequestParam(value = "photoSize", required = false) String photoSize,// 사진 파일 경로 추가
                               Model model) {
        // 수정 페이지로 이동하면서 데이터 전달
        model.addAttribute("recipeNumber", recipeNumber);
        model.addAttribute("recipeTitle", recipeTitle);
        model.addAttribute("recipeText", recipeText);
        model.addAttribute("photoLocal", photoLocal); // photoLocal 추가
        model.addAttribute("photoOriginal", photoOriginal); // photoOriginal 추가
        model.addAttribute("photoSize", photoSize);

        return "/recipe/myRecipeModify"; // 수정 페이지의 템플릿 이름
    }

    // 나만의 레시피 수정 후 목록 페이지로 이동
    @PostMapping("/saveMyRecipeUpdate")
    public String saveMyRecipeUpdate(@ModelAttribute MyRecipeUpdateDTO myRecipeUpdateDTO,RedirectAttributes redirectAttributes) {
        try {
            // 챗봇 레시피와 사진을 업데이트하는 서비스 메서드 호출
            recipeService.updateRecipeAndPhoto(myRecipeUpdateDTO);
            redirectAttributes.addAttribute("recipeNumber", myRecipeUpdateDTO.getRecipeNumber());
            // 수정이 완료되면 상세 페이지로 리다이렉트
            return "redirect:/recipe/myDetailPage";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정에 실패했습니다.");
            return "redirect:/recipe/myRecipeModify";
        }
    }

    //챗봇의 레시피 수정 이동
    @GetMapping("/chatBotRecipeModify")
    public String chatBotRecipeModify(@RequestParam("recipeNumber4") Long recipeNumber,
                                      @RequestParam("recipeTitle") String recipeTitle,
                                      @RequestParam("recipeText") String recipeText,
                                      @RequestParam(value = "photoLocal", required = false) String photoLocal, // 사진 파일 경로 추가
                                      @RequestParam(value = "photoOriginal", required = false) String photoOriginal,
                                      @RequestParam(value = "photoSize", required = false) String photoSize,// 사진 파일 경로 추가
                                      Model model) {
        // 수정 페이지로 이동하면서 데이터 전달
        model.addAttribute("recipeNumber", recipeNumber);
        model.addAttribute("recipeTitle", recipeTitle);
        model.addAttribute("recipeText", recipeText);
        model.addAttribute("photoLocal", photoLocal); // photoLocal 추가
        model.addAttribute("photoOriginal", photoOriginal); // photoOriginal 추가
        model.addAttribute("photoSize", photoSize);

        return "recipe/chatBotRecipeModify"; // 수정 페이지의 템플릿 이름
    }

    // 챗봇 레시피 수정 후 목록 페이지로 이동
    @PostMapping("/saveChatBotRecipeUpdate")
    public String saveChatBotRecipeUpdate(@ModelAttribute ChatBotRecipeUpdateDTO chatBotRecipeUpdateDTO,RedirectAttributes redirectAttributes) {
        try {
            // 챗봇 레시피와 사진을 업데이트하는 서비스 메서드 호출
            recipeService.updateChatBot(chatBotRecipeUpdateDTO);
            redirectAttributes.addAttribute("recipeNumber", chatBotRecipeUpdateDTO.getRecipeNumber());
            // 수정이 완료되면 상세 페이지로 리다이렉트
            return "redirect:/recipe/chatBotDetailPage";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정에 실패했습니다.");
            return "redirect:/recipe/chatBotRecipeModify";
        }
    }

}
