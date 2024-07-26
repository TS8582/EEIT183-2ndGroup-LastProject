package com.playcentric.model.member;

import org.springframework.data.jpa.repository.JpaRepository;


public interface GoogleLoginRepository extends JpaRepository<GoogleLogin, String> {

    GoogleLogin findByEmail(String email);

}
