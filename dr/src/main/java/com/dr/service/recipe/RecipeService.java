package com.dr.service.recipe;

import com.dr.dto.recipe.*;
import com.dr.mapper.recipe.RecipeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;

    //    나만의 레시피 최신순
    public List<MyRecipeListDTO> findAllRecipes() {
        return recipeMapper.selectAllPages();  // selectAllPage()는 페이지네이션 없이 전체 목록을 조회합니다.
    }

    //    나만의 레시피 추천순
    public List<MyRecipeListDTO> findAllRecipesGood() {
        return recipeMapper.selectRecipesGood();
    }


    //챗봇의 레시피 최신순
    public List<ChatBotRecipeListDTO> findAllRecipes1() {
        return recipeMapper.selectAllPages1();  // selectAllPage()는 페이지네이션 없이 전체 목록을 조회합니다.
    }

    //챗봇의 레시피 추천순
    public List<ChatBotRecipeListDTO> findAllRecipes1Good() {
        return recipeMapper.selectAllPagesGood1();
    }

    //나만의레시피상세페이지
    public MyRecipeDetailDTO findMyRecipeDetail(Long recipeNumber) {
        return recipeMapper.selectMyRecipeDetail(recipeNumber);  // 특정 레시피의 상세 정보를 조회합니다.
    }

    //챗봇레시피 상세페이지
    public ChatBotRecipeDetailDTO findChatBotRecipeDetail(Long recipeNumber) {
        return recipeMapper.selectChatBotRecipeDetail(recipeNumber);  // 특정 레시피의 상세 정보를 조회합니다.
    }

    // 나만의 레시피 댓글 작성
    public void insertMyRecipeComment(MyRecipeCommentDTO myRecipeCommentDTO) {
        recipeMapper.insertReply(myRecipeCommentDTO);
    }

    //챗봇 레시피 댓글 작성
    public void insertChatBotRecipeComment(ChatBotRecipeCommentDTO chatBotRecipeCommentDTO) {
        recipeMapper.insertChatBotReply(chatBotRecipeCommentDTO);
    }

    //나만의 레시피 댓글 수정
    public void updateMyRecipeComment(Long replyNumber, String replyText) {
        log.info("=========서비스 수정 확인======");
        MyRecipeCommentDTO myRecipeCommentDTO = new MyRecipeCommentDTO();
        myRecipeCommentDTO.setReplyNumber(replyNumber);
        myRecipeCommentDTO.setReplyText(replyText);
        log.info("==== replyNumber 확인 service ===");
        log.info(myRecipeCommentDTO.getReplyNumber() + "확인");
        log.info(myRecipeCommentDTO.getReplyText() + "확인");
        recipeMapper.updateMyReply(myRecipeCommentDTO);
    }

    // 챗봇 레시피 댓글 수정
    public void updateChatBotRecipeComment(Long replyNumber, String replyText) {
        log.info("=========서비스 수정 확인======");
        ChatBotRecipeCommentDTO chatBotRecipeCommentDTO = new ChatBotRecipeCommentDTO();
        chatBotRecipeCommentDTO.setReplyNumber(replyNumber);
        chatBotRecipeCommentDTO.setReplyText(replyText);
        log.info("==== replyNumber 확인 service ===");
        log.info(chatBotRecipeCommentDTO.getReplyNumber() + "확인");
        log.info(chatBotRecipeCommentDTO.getReplyText() + "확인");
        recipeMapper.updateChatBotReply(chatBotRecipeCommentDTO);
    }

    //나만의 레시피 댓글 삭제
    public void deleteMyRecipeComment(Long replyNumber) {
        recipeMapper.deleteMyReply(replyNumber);
    }
    // 챗봇레시피 댓글 삭제
    public void deleteChatBotRecipeComment(Long replyNumber) {
        recipeMapper.deleteChatBotReply(replyNumber);
    }

    //나만의 레시피 댓글조회
    public List<MyRecipeCommentDTO> selectMyRecipeComment(Long recipeNumber) {
        // 특정 레시피의 댓글 목록을 조회하여 반환
        return recipeMapper.selectMyRecipeComment(recipeNumber);
    }

    //챗봇 레시피 댓글조회
    public List<ChatBotRecipeCommentDTO> selectChatBotRecipeComment(Long recipeNumber) {
        return recipeMapper.selectChatBotRecipeComment(recipeNumber);
    }

    //나만의 레시피 글쓰기
    @Transactional  // 트랜잭션 관리
    public void saveRecipe(MyRecipeWriteDTO myRecipeWriteDTO, RecipePhotoDTO recipePhotoDTO) {
        // 1. 레시피 정보 먼저 저장 (RECIPE 테이블)
        recipeMapper.insertMyRecipe(myRecipeWriteDTO);

        // 2. RECIPE_NUMBER와 USER_NUMBER를 설정하여 RecipePhotoDTO에 추가
        Long recipeNumber = myRecipeWriteDTO.getRecipeNumber();
        Long userNumber = myRecipeWriteDTO.getUserNumber();

        recipePhotoDTO.setRecipeNumber(recipeNumber);  // RECIPE_NUMBER 설정
        recipePhotoDTO.setUserNumber(userNumber);      // USER_NUMBER 설정

        // 3. 사진 정보 저장 (PHOTO 테이블)
        recipeMapper.insertMyPhoto(recipePhotoDTO);
    }

    @Transactional  // 트랜잭션 관리
    public void saveChatBotRecipe(ChatBotRecipeWriteDTO chatBotRecipeWriteDTO, RecipePhotoDTO recipePhotoDTO) {
        // 1. 레시피 정보 먼저 저장 (RECIPE 테이블)
        recipeMapper.insertChatBotRecipe(chatBotRecipeWriteDTO);

        // 2. RECIPE_NUMBER와 USER_NUMBER를 설정하여 RecipePhotoDTO에 추가
        Long recipeNumber = chatBotRecipeWriteDTO.getRecipeNumber();
        Long userNumber = chatBotRecipeWriteDTO.getUserNumber();

        recipePhotoDTO.setRecipeNumber(recipeNumber);  // RECIPE_NUMBER 설정
        recipePhotoDTO.setUserNumber(userNumber);      // USER_NUMBER 설정

        // 3. 사진 정보 저장 (PHOTO 테이블)
        recipeMapper.insertChatBotPhoto(recipePhotoDTO);
    }

    // 챗봇 레시피 글 작성 시 환경기여 점수 10점 추가
    public void insertScoreByRecipe(ScoreCheckDTO scoreCheckDTO) {
        log.info("챗봇 레시피 작성 시 기여점수 올라가니?");
        // 환경기여 점수 10점 추가
        scoreCheckDTO.setScoreGet(10L);  // 점수를 10점으로 설정
        recipeMapper.insertScoreByRecipe(scoreCheckDTO);  // DB에 점수 정보 저장
    }

    //나만의 레시피 추천받았을 시 점수 5점추가
    public void insertScorerecommand(ScoreCheckDTO scoreCheckDTO) {
        scoreCheckDTO.setScoreGet(5L);  // 점수를 5점으로 설정
        recipeMapper.insertScorerecommand(scoreCheckDTO);  // DB에 점수 정보 저장
    }

    //나만의 레시피 추천해제 시 점수 5점 제거
    public void deleteScorerecommand(ScoreCheckDTO scoreCheckDTO) {
        recipeMapper.deleteScorerecommand(scoreCheckDTO);  // DB에 점수 정보 저장
    }

    // 나만의 레시피 추천 수 증가
    public void addGood(MyRecipeGoodDTO myRecipeGoodDTO) {
        recipeMapper.increaseGoodCount(myRecipeGoodDTO);
    }

    // 챗봇 레시피 추천 수 증가
    public void ChatBotAddGood(ChatBotRecipeGoodDTO chatBotRecipeGoodDTO) {
        recipeMapper.ChatBotincreaseGoodCount(chatBotRecipeGoodDTO);
    }

    // 나만의 레시피 추천 수 감소
    public void removeGood(MyRecipeGoodDTO myRecipeGoodDTO) {
        recipeMapper.decreaseGoodCount(myRecipeGoodDTO);
    }

    // 챗봇 레시피 추천 수 감소
    public void ChatBotRemoveGood(ChatBotRecipeGoodDTO chatBotRecipeGoodDTO) {
        recipeMapper.ChatBotdecreaseGoodCount(chatBotRecipeGoodDTO);
    }

    // 찜 추가 메서드
    public void addSteam(RecipeSteamDTO recipeSteamDTO) {
        recipeMapper.addSteam(recipeSteamDTO);
    }

    // 찜 삭제 메서드
    public void removeSteam(RecipeSteamDTO recipeSteamDTO) {
        recipeMapper.removeSteam(recipeSteamDTO);
    }

    // 신고를 위한 메서드
    public void report(RecipeReportDTO reportDTO) {
        recipeMapper.report(reportDTO);
    }

    // 나만의 레시피 삭제
    @Transactional
    public void deleteRecipeAndPhoto(Long recipeNumber) {
        recipeMapper.deleteRecipe(recipeNumber);
        recipeMapper.deletePhoto(recipeNumber);
    }

    //챗봇 레시피 삭제
    @Transactional
    public void deleteChatBot(Long recipeNumber) {
        recipeMapper.deleteChatBotRecipe(recipeNumber);
        recipeMapper.deleteChatBotPhoto(recipeNumber);
    }

    //나만의 레시피 수정
    @Transactional
    public void updateRecipeAndPhoto(MyRecipeUpdateDTO myRecipeUpdateDTO) {
        recipeMapper.updateRecipe(myRecipeUpdateDTO);
        recipeMapper.updatePhoto(myRecipeUpdateDTO);
    }

    // 챗봇 레시피 수정
    @Transactional
    public void updateChatBot(ChatBotRecipeUpdateDTO chatBotRecipeUpdateDTO) {
        recipeMapper.updateChatBotRecipe(chatBotRecipeUpdateDTO);
        recipeMapper.updateChatBotPhoto(chatBotRecipeUpdateDTO);
    }
}
