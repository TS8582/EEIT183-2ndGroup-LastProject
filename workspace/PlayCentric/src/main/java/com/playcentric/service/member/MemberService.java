package com.playcentric.service.member;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.playcentric.config.NgrokConfig;
import com.playcentric.model.ImageLibRepository;
import com.playcentric.model.member.GoogleLogin;
import com.playcentric.model.member.GoogleLoginRepository;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private GoogleLoginRepository googleRepository;

	@Autowired
	private ImageLibRepository imageLibRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private NgrokConfig ngrokConfig;
	
	
	public void setGoogleVerified(String googleId, Boolean verified){
		Optional<GoogleLogin> optional = googleRepository.findById(googleId);
		if (optional.isPresent()) {
			GoogleLogin googleLogin = optional.get();
			googleLogin.setVerifiedEmail(verified);
		}
	}
	
	public Member memAddGoogle(GoogleLogin memGoogle){
		Integer memberId = memberRepository.findByEmail(memGoogle.getEmail()).getMemId();
		return memAddGoogle(memberId, memGoogle);
	}

	public Member memAddGoogle(Integer memberId, GoogleLogin memGoogle){
		Optional<Member> optional = memberRepository.findById(memberId);
		if (optional.isPresent()) {
			Member member = optional.get();
			member.setGoogeId(memGoogle.getGoogleId());
			member.setGoogleLogin(memGoogle);
			return memberRepository.save(member);
		}
		return null;
	}
	
	public Member addGoogleMem(GoogleLogin memGoogle) {
		Member newMember = new Member();
		String password = "login by google";
		// memGoogle = googleRepository.save(memGoogle);
		newMember.setGoogeId(memGoogle.getGoogleId());
		newMember.setAccount(memGoogle.getEmail());
		newMember.setPassword(password);
		newMember.setNickname(memGoogle.getName());
		newMember.setMemName(memGoogle.getName());
		newMember.setEmail(memGoogle.getEmail());
		newMember.setGoogleLogin(memGoogle);
		return addMember(newMember);
	}
	
	public Member addMember(Member newMember) {
		if (!newMember.getPassword().contains("login")) {
			String encodedPwd = passwordEncoder.encode(newMember.getPassword());
			newMember.setPassword(encodedPwd);
		}
		newMember.setTotalSpent(0);
		if (newMember.getRole()==null) {
			newMember.setRole((short)0);
		}
		newMember.setStatus((short)0);
		newMember.setPoints(0);
		return memberRepository.save(newMember);
	}

	public Member updateMember(Member member, Member originMem){
		originMem.setAccount(member.getAccount());
		originMem.setAddress(member.getAddress());
		originMem.setBirthday(member.getBirthday());
		if (!originMem.getEmail().equals(member.getEmail())) {
			originMem.setEmailVerified(false);
			originMem.setEmailVerifyToken(null);
			originMem.setEmail(member.getEmail());
		}
		originMem.setGender(member.getGender());
		originMem.setMemName(member.getMemName());
		originMem.setNickname(member.getNickname());
		originMem.setPhone(member.getPhone());
		if (member.getPhoto()!=null) {
			Integer originPhoto = originMem.getPhoto();
			if (originPhoto != null) {
				imageLibRepository.deleteById(originPhoto);
			}
			originMem.setPhoto(member.getPhoto());
		}
		originMem.setRole(member.getRole());
		return memberRepository.save(originMem);
	}

	public boolean deleteMemById(Integer memId){
		Optional<Member> optional = memberRepository.findById(memId);
		if (optional.isPresent()) {
			Member member = optional.get();
			if (member.getStatus()==0) {
				member.setStatus((short)1);
				memberRepository.save(member);
				return true;
			}
		}
		return false;
	}

	public Member verifyEmail(Integer memId, String token){
		Optional<Member> optional = memberRepository.findById(memId);
		if (optional.isPresent()) {
			Member member = optional.get();
			member.setEmailVerifyToken(token);
			return memberRepository.save(member);
		}
		return null;
	}

	public Member verifyEmail(String token){
		Member member = memberRepository.findByEmailVerifyToken(token);
		if (member == null) {
			return null;
		}
		member.setEmailVerified(true);
		member.setEmailVerifyToken(null);
		return memberRepository.save(member);
	}

	public Page<Member> findByKeyword(String keyword,Integer pageNum){
		PageRequest pageable = PageRequest.of(pageNum-1, 6, Sort.Direction.ASC, "memId");
		// return memberRepository.findByStatusAndAccountContainingOrNicknameContainingOrMemNameContainingOrEmailContaining((short)0,keyword,keyword,keyword,keyword,pageable);
		return memberRepository.findByKeyword("%"+keyword.toLowerCase()+"%", pageable);
	}

	public Page<Member> findByPage(Integer pageNum){
		PageRequest pageable = PageRequest.of(pageNum-1, 6, Sort.Direction.ASC, "memId");
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
		Member member = memberRepository.findByAccountAndStatus(account,(short)0);
		if (member==null) {
			return member;
		}
		String encodedPassword = member.getPassword();

		return passwordEncoder.matches(password, encodedPassword)? memberRepository.save(member):null;
	}

	public Member findById(Integer memId){
		Optional<Member> optional = memberRepository.findById(memId);
		return optional.isPresent()? optional.get():null;
	}

	public Member findByEmail(String email){
		return memberRepository.findByEmail(email);
	}

	public Member findByGoogleId(String googleId){
		System.err.println("沒有更新登入時間");
		return memberRepository.findByGoogeId(googleId);
	}

	public Member findByPwdToken(String token){
		return memberRepository.findByPasswordToken(token);
	}

	public Member memberLogin(Member member){
		member.setLastLogin(new Date());
		System.err.println("更新登入時間");
		member.setLoginToken(UUID.randomUUID().toString());
		System.err.println("加入登入Token");
		return memberRepository.save(member);
	}

	public LoginMemDto getLoginMember(String loginToken){
		Member member = memberRepository.findByLoginToken(loginToken);
		return setLoginDto(member);
	}

	public LoginMemDto checkLoginMember(LoginMemDto loginMember){
		if (loginMember==null) {
			return null;
		}
		Member originMem = memberRepository.findByLoginToken(loginMember.getLoginToken());
		return setLoginDto(originMem);
	}

	public Member changePassword(Integer memId, String token){
		Optional<Member> optional = memberRepository.findById(memId);
		if (optional.isPresent()) {
			Member member = optional.get();
			member.setPasswordToken(token);
			return memberRepository.save(member);
		}
		return null;
	}

	public Member changePassword(String password, String token){
		Member member = memberRepository.findByPasswordToken(token);
		String encodedPwd = passwordEncoder.encode(password);
		member.setPassword(encodedPwd);
		member.setPasswordToken(null);
		return memberRepository.save(member);
	}

	public LoginMemDto setLoginDto(Member member){
		if (member==null) {
			return null;
		}
        String url = ngrokConfig.getUrl();
		LoginMemDto loginMember = new LoginMemDto();
		loginMember.setAccount(member.getAccount());
		loginMember.setLastLogin(member.getLastLogin());
		loginMember.setMemId(member.getMemId());
		loginMember.setMemName(member.getMemName());
		loginMember.setNickname(member.getNickname());
		String photoPath = member.getPhoto()!=null? url+"/PlayCentric/imagesLib/image"+member.getPhoto():
                member.getGoogleLogin()!=null? member.getGoogleLogin().getPhoto():
                url+"/PlayCentric/imagesLib/image144";
		loginMember.setPhoto(photoPath);
		loginMember.setPoints(member.getPoints());
		loginMember.setRole(member.getRole());
		loginMember.setLoginToken(member.getLoginToken());

		return loginMember;
	}
	
	public void save(Member member) {
		memberRepository.save(member);
	}
}
