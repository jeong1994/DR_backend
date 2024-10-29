package com.dr.mapper.recipe;

import com.dr.dto.recipe.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecipeMapper {
    // 전체 레시피 목록 조회 메서드
    List<MyRecipeListDTO> selectAllPages();

    List<ChatBotRecipeListDTO> selectAllPages1();

    MyRecipeDetailDTO selectMyRecipeDetail(@Param("recipeNumber") Long recipeNumber);

    ChatBotRecipeDetailDTO selectChatBotRecipeDetail(@Param("recipeNumber") Long recipeNumber);

    void insertMyRecipe(MyRecipeWriteDTO myRecipeWriteDTO);
}
