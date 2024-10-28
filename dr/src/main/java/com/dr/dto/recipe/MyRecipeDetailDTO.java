package com.dr.dto.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyRecipeDetailDTO {
    private Long recipeNumber;           // 레시피 번호 (ID 대신 사용)
    private String recipeTitle;          // 레시피 제목
    private String recipeText;           // 레시피 내용
    private String photoLocal;           // 이미지 경로 (photoOriginal -> photoLocal로 수정)
    private String userNickName;         // 작성자
    private String recipeWriteDate;      // 작성일
    private int goodCount;               // 추천수
    // 댓글 관련 필드는 필요시 추가
}