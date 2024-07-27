package com.playcentric.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playcentric.config.GoogleOAuth2Config;
import com.playcentric.config.NgrokConfig;
import com.playcentric.model.member.GoogleLogin;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/member")
@SessionAttributes(names = { "loginMember" })
public class GoogleOAuth2Controller {

	@Autowired
	private GoogleOAuth2Config googleOAuth2Config;

	@Autowired
	private NgrokConfig ngrokConfig;

	@Autowired
	private MemberService memberService;

	private final String scope = "https://www.googleapis.com/auth/userinfo.eamil";

	@GetMapping("/google-login")
	public String googleLogin(@RequestParam(required = false) Boolean addCookie) {
		String redirectUri = googleOAuth2Config.getRedirectUri();
		if (addCookie != null) {
			redirectUri += addCookie ? "/addCookie" : "";
		}
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
				+ "client_id=" + googleOAuth2Config.getClientId()
				+ "&response_type=code"
				+ "&redirect_uri=" + redirectUri
				+ "&state=state"
				+ "&scope=openid%20email%20profile";
		System.err.println(redirectUri);
		return "redirect:" + authUrl;
	}

	@GetMapping("/google-login/addGoogle")
	public String getMethodName() {
		String redirectUri = googleOAuth2Config.getRedirectUri() + "/addGoogle";
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
				+ "client_id=" + googleOAuth2Config.getClientId()
				+ "&response_type=code"
				+ "&redirect_uri=" + redirectUri
				+ "&state=state"
				+ "&scope=openid%20email%20profile";
		System.err.println(redirectUri);
		return "redirect:" + authUrl;
	}

	@GetMapping("/google-callback")
	public String googleCallback(@RequestParam(required = false) String code, Model model,
			RedirectAttributes redirectAttributes) {
		if (model.getAttribute("loginMember") != null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "已登入!");
			return "redirect:/";
		}
		if (code == null) {
			String authUrl = googleLogin(false);
			authUrl = authUrl.substring(0, authUrl.lastIndexOf("openid%20email%20profile"))
					+ scope;
			return authUrl;
		}
		try {
			return googleCallBackFuntion(code, model, redirectAttributes, googleOAuth2Config.getRedirectUri());
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("redirectMsg", "登入失敗!");
			return "redirect:/member/login";
		}
	}

	@GetMapping("/google-callback/addCookie")
	public String googleCallbackAddCookie(@RequestParam(required = false) String code, Model model,
			RedirectAttributes redirectAttributes) {
		if (model.getAttribute("loginMember") != null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "已登入!");
			return "redirect:/";
		}
		if (code == null) {
			String authUrl = googleLogin(true);
			authUrl = authUrl.substring(0, authUrl.lastIndexOf("openid%20email%20profile"))
					+ scope;
			return authUrl;
		}
		try {
			String returnUrl = googleCallBackFuntion(code, model, redirectAttributes,
					googleOAuth2Config.getRedirectUri() + "/addCookie") + "/addCookie";
			System.err.println("google-call-back-returnUrl=" + returnUrl);
			return returnUrl;
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("redirectMsg", "登入失敗!");
			return "redirect:/member/login";
		}
	}

	@GetMapping("/google-callback/addGoogle")
	public String googleCallbackAddGoogle(@RequestParam(required = false) String code, Model model,
			RedirectAttributes redirectAttributes) {
		if (model.getAttribute("loginMember") == null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "請先登入!");
			return "redirect:/";
		}
		if (code == null) {
			String authUrl = googleLogin(true);
			authUrl = authUrl.substring(0, authUrl.lastIndexOf("openid%20email%20profile"))
					+ scope;
			return authUrl;
		}
		try {
			String returnUrl = googleCallBackFuntion(code, model, redirectAttributes,
					googleOAuth2Config.getRedirectUri() + "/addGoogle");
			System.err.println("google-call-back-returnUrl=" + returnUrl);
			return returnUrl;
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("redirectMsg", "綁定失敗!");
			return "redirect:/member/memInfo";
		}
	}

	@GetMapping("/google-loginOK/{loginToken}")
	public String googleLogin(@PathVariable String loginToken, Model model) {
		LoginMemDto loginMember = memberService.getLoginMember(loginToken);
		if (loginMember != null) {
			model.addAttribute("loginMember", loginMember);
		}
		return "redirect:/member/loginSuccess";
	}

	@GetMapping("/google-loginOK/{loginToken}/addCookie")
	public String googleLoginAddCookie(@PathVariable String loginToken, Model model, HttpServletResponse response) {
		LoginMemDto loginMember = memberService.getLoginMember(loginToken);
		if (loginMember != null) {
			model.addAttribute("loginMember", loginMember);

			Cookie cookie = new Cookie("loginToken", loginToken);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/PlayCentric");
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			response.addCookie(cookie);

		}
		return "redirect:/member/loginSuccess";
	}

	private String googleCallBackFuntion(String code, Model model, RedirectAttributes redirectAttributes,
			String redirectUri)
			throws Exception {
		RestClient restClient = RestClient.create();
		// 設定request params
		String requestBody = UriComponentsBuilder.newInstance()
				.queryParam("code", code)
				.queryParam("client_id", googleOAuth2Config.getClientId())
				.queryParam("client_secret", googleOAuth2Config.getClientSecret())
				.queryParam("redirect_uri", redirectUri)
				.queryParam("grant_type", "authorization_code")
				.build()
				.getQuery();
		if (requestBody == null) {
			throw new Exception("取得token的request設定錯誤!");
		}
		// 向google發送request取得token
		String credentials = restClient.post()
				.uri("https://oauth2.googleapis.com/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(requestBody)
				.retrieve()
				.body(String.class);
		// 取得token字串
		JsonNode jsonNode = new ObjectMapper().readTree(credentials);
		String accessToken = jsonNode.get("access_token").asText();
		String idToken = jsonNode.get("id_token").asText();
		// 利用token再次向google發送請求，找user
		String payloadResponse = restClient.get()
				.uri("https://www.googleapis.com/oauth2/v1/userinfo?"
						+ "alt=json&access_token=" + accessToken)
				.header("Authorization", "Bearer" + idToken)
				.retrieve().body(String.class);
		// System.err.println(payloadResponse);
		// 取得該帳號email
		JsonNode userInfo = new ObjectMapper().readTree(payloadResponse);
		System.err.println(userInfo);
		String userEmail = userInfo.get("email").asText();
		String googleId = userInfo.get("id").asText();
		boolean verified = userInfo.get("verified_email").asBoolean();
		String googlePhoto = userInfo.get("picture").asText();
		String googleName = userInfo.get("name").asText();

		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		boolean hasGoogle = memberService.checkGoogleExist(googleId);
		boolean hasEmail = memberService.checkEmailExist(userEmail);

		GoogleLogin memGoogle = new GoogleLogin();
		memGoogle.setGoogleId(googleId);
		memGoogle.setEmail(userEmail);
		memGoogle.setName(googleName);
		memGoogle.setPhoto(googlePhoto);
		memGoogle.setVerifiedEmail(verified);

		// 檢查是否已經有此帳號
		Member member = null;
		if (loginMember != null) {
			String redirectMsg = "";
			hasEmail = memberService.checkEmailExist(userEmail, loginMember.getMemId());
			if (hasGoogle) {
				redirectMsg = "此Google已被綁定!";
			}else if (hasEmail) {
				redirectMsg = "此信箱已被註冊!";
			}else {
				redirectMsg = "Google綁定成功!";
				member = memberService.memAddGoogle(loginMember.getMemId(), memGoogle);
			}
			System.err.println(redirectMsg);
			redirectAttributes.addFlashAttribute("redirectMsg", redirectMsg);
			return "redirect:/member/memInfo";
		}
		if (hasGoogle) { // 利用google帳號登入
			System.err.println("利用google登入");
			memberService.setGoogleVerified(googleId, verified);
			member = memberService.findByGoogleId(googleId);
		} else if (hasEmail) {
			System.err.println("利用google登入已註冊帳號");
			member = memberService.memAddGoogle(memGoogle);
		} else {
			System.err.println("註冊新google帳號");
			member = memberService.addGoogleMem(memGoogle);
		}
		if (userEmail.equals(member.getEmail())) {
			member.setEmailVerified(verified);
		}
		member = memberService.memberLogin(member);
		System.err.println(member);
		return "redirect:" + ngrokConfig.getUrl() + "/PlayCentric/member/google-loginOK/" + member.getLoginToken();
	}

}
