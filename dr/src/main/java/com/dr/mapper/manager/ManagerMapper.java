package com.dr.mapper.manager;

import com.dr.dto.manager.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.List;

@Mapper
public interface ManagerMapper {

   Optional<ManagerSessionDTO> managerLogin(@Param("managerEmail") String managerEmail, @Param("managerPw") String managerPw);

   DashBoardDTO dashBoardInfo();

   List <ManagerDTO> managerInfo();

   List <ManagerUserDTO> manageUser();

   boolean userOut(Integer userNumber);

   boolean userPause(Integer userNumber);

   ManagerUserDTO userSearch(int userNumber);

   List <ManagerBoardDTO> showBoard();

   boolean boardDelete(Integer boardNumber);

   ManagerBoardDTO boardSearch(int boardNumber);

   List <ManagerRecipeDTO> showRecipe();

   boolean recipeDelete(Integer recipeNumber);

   ManagerRecipeDTO recipeSearch(int recipeNumber);

   List <ManagerCommentDTO> showReply();

   boolean replyDelete(Integer replyNumber);

   ManagerCommentDTO replySearch(int replyNumber);

   List<ManagerPointDTO> showPoint();

   boolean pointDelete(Integer pointNumber);

   boolean takePoint(Integer pointNumber);

   List<ManagerPointDTO> pointSearch(String userNickName);

   List<ManagerReportDTO> showReport();

   boolean reportDelete(Integer sirenNumber);

   List<ManagerProductDTO> showProduct();

   boolean productDelete(String productName);

   void productRegister(ManagerRegisterDTO managerRegisterDTO);



}
