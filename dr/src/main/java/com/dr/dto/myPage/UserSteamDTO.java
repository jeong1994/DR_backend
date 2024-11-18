package com.dr.dto.myPage;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserSteamDTO {
    private Long userNumber;
    private Long recipeNumber;
    private String recipeTitle;
    private String userNickname;
    private String photoOriginal;
    private String recipeType;
}
