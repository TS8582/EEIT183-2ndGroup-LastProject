package com.playcentric.model.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "googleLogin")
public class GoogleLogin {

    @Id
    private String googleId;
    private String name;
    private String photo;
    private String email;
    private Boolean verifiedEmail;

    @PrePersist
    @PostLoad
    public void initializeGoogleLogin(){
        setEmailVerified(this.verifiedEmail);
    }

    public void setEmailVerified(Boolean verifiedEmail){
        this.verifiedEmail = verifiedEmail==null? false:verifiedEmail;
    }
}
