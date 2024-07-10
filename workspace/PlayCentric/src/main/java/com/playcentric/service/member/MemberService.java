package com.playcentric.service.member;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.playcentric.model.member.GoogleLogin;
import com.playcentric.model.member.GoogleLoginRepository;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private GoogleLoginRepository googleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Member memAddGoogle(GoogleLogin memGoogle){
		Integer memberId = memberRepository.findByEmail(memGoogle.getEmail()).getMemId();
		return memAddGoogle(memberId, memGoogle);
	}

	public Member memAddGoogle(Integer memberId, GoogleLogin memGoogle){
		Optional<Member> optional = memberRepository.findById(memberId);
		if (optional.isPresent()) {
			Member member = optional.get();
			member.setGoogeId(memGoogle.getGoogleId());
			googleRepository.save(memGoogle);
			return memberRepository.save(member);
		}
		return null;
	}
	
	public Member addGoogleMem(GoogleLogin memGoogle) {
		Member newMember = new Member();
		String password = "login by google";
		memGoogle = googleRepository.save(memGoogle);
		newMember.setGoogeId(memGoogle.getGoogleId());
		newMember.setAccount(memGoogle.getEmail());
		newMember.setPassword(password);
		newMember.setNickname(memGoogle.getName());
		newMember.setMemName(memGoogle.getName());
		newMember.setEmail(memGoogle.getEmail());
		return addMember(newMember);
	}
	
	public Member addMember(Member newMember) {
		if (!newMember.getPassword().contains("login")) {
			String encodedPwd = passwordEncoder.encode(newMember.getPassword());
			newMember.setPassword(encodedPwd);
		}
		newMember.setTotalSpent(0);
		newMember.setRole((short)0);
		newMember.setStatus((short)0);
		newMember.setPoints(0);
		return memberRepository.save(newMember);
	}

	public Page<Member> findByPage(Integer pageNum){
		PageRequest pageable = PageRequest.of(pageNum-1, 3, Sort.Direction.ASC, "memId");
		return memberRepository.findByStatus((short)0,pageable);
	}
	
	public boolean checkAccountExist(String account) {
		return memberRepository.findByAccount(account)!=null;
	}
	
	public boolean checkEmailExist(String email) {
		return memberRepository.findByEmail(email)!=null;
	}
	
	public boolean checkGoogleExist(String googleId) {
		return googleRepository.findById(googleId).isPresent();
	}
	
	public Member checkLogin(String account, String password) {
		Member member = memberRepository.findByAccount(account);
		if (member==null) {
			return member;
		}
		String encodedPassword = member.getPassword();
		return passwordEncoder.matches(password, encodedPassword)? member:null;
	}

	public Member findByEmail(String email){
		return memberRepository.findByEmail(email);
	}

	public Member findByGoogleId(String googleId){
		return memberRepository.findByGoogeId(googleId);
	}
}
