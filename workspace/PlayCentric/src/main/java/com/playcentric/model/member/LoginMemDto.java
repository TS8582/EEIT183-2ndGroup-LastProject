package com.playcentric.model.member;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class LoginMemDto {

    private Integer memId;
    private String account;
    // private String password;
    // private String email;
    private String nickname;
    private String memName;
	// private Date birthday;
    // private String phone;
    // private String address;
    // private String googeId;
    // private String facebookId;
    // private String twitterId;
    // private Integer totalSpent;
	// private Date registDate;
	private Date lastLogin;
    private Short role;
    private String photo;
    // private Short gender;
    // private Short status;
    private Integer points;


    public LoginMemDto(Member member) {
        this.account = member.getAccount();
        this.lastLogin = member.getLastLogin();
        this.memId = member.getMemId();
        this.memName = member.getMemName();
        this.nickname = member.getNickname();
        this.photo = member.getPhoto()!=null? "http://localhost:8080/PlayCentric/imagesLib/image"+member.getPhoto():
                member.getGoogleLogin()!=null? member.getGoogleLogin().getPhoto():
                "http://localhost:8080/PlayCentric/imagesLib/image144";
        this.points = member.getPoints();
        this.role = member.getRole();
    }
}