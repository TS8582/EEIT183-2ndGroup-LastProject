package com.playcentric.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playcentric.config.GoogleOAuth2Config;
import com.playcentric.model.member.GoogleLogin;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberDto;
import com.playcentric.service.member.MemberService;

@Controller
@RequestMapping("/member")
@SessionAttributes(names = { "loginMember" })
public class GoogleOAuth2Controller {

	@Autowired
	private GoogleOAuth2Config googleOAuth2Config;

	@Autowired
	private MemberService memberService;

	private final String scope = "https://www.googleapis.com/auth/userinfo.eamil";

	@GetMapping("/google-login")
	public String googleLogin() {
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
				+ "client_id=" + googleOAuth2Config.getClientId()
				+ "&response_type=code"
				+ "&redirect_uri=" + googleOAuth2Config.getRedirectUri()
				+ "&state=state"
				+ "&scope=openid%20email%20profile";
		return "redirect:" + authUrl;
	}

	@GetMapping("/google-callback")
	public String googleCallback(@RequestParam(required = false) String code, Model model,
			RedirectAttributes redirectAttributes) {
		if (code == null) {
			String authUrl = googleLogin();
			authUrl = authUrl.substring(0, authUrl.lastIndexOf("openid%20email%20profile"))
					+ scope;
			return authUrl;
		}
		RestClient restClient = RestClient.create();
		try {
			// 設定request params
			String requestBody = UriComponentsBuilder.newInstance()
					.queryParam("code", code)
					.queryParam("client_id", googleOAuth2Config.getClientId())
					.queryParam("client_secret", googleOAuth2Config.getClientSecret())
					.queryParam("redirect_uri", googleOAuth2Config.getRedirectUri())
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
			String userEmail = userInfo.get("email").asText();
			String googleId = userInfo.get("id").asText();

			MemberDto loginMember = (MemberDto) model.getAttribute("loginMember");
			boolean hasGoogle = memberService.checkGoogleExist(userEmail);
			boolean hasEmail = memberService.checkEmailExist(userEmail);
			// 檢查是否已經有此帳號
			Member member = null;
			if (hasGoogle) {
				member = memberService.findByGoogleId(googleId);
			} else {
				GoogleLogin memGoogle = new GoogleLogin();
				String googlePhoto = userInfo.get("picture").asText();
				String googleName = userInfo.get("name").asText();
				memGoogle.setGoogleId(googleId);
				memGoogle.setEmail(userEmail);
				memGoogle.setName(googleName);
				memGoogle.setPhoto(googlePhoto);
				if (loginMember != null) {
					if (hasEmail) {
						redirectAttributes.addFlashAttribute("errorMsg", "此信箱已被註冊!");
						// 此處要改成使用者綁定頁面
						return "redirect:/member/home";
					}
					member = memberService.memAddGoogle(loginMember.getMemId(), memGoogle);
					redirectAttributes.addFlashAttribute("okMsg", "Google綁定成功!");
					// 此處要改成使用者綁定頁面
					return "redirect:/member/home";
				} else if (hasEmail) {
					member = memberService.memAddGoogle(memGoogle);
				} else {
					member = memberService.addGoogleMem(memGoogle);
				}
			}
			model.addAttribute("loginMember", new MemberDto(member));
			redirectAttributes.addFlashAttribute("okMsg", member.getAccount() + "登入成功!");
			return "redirect:/member/home";

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "登入失敗!");
			return "redirect:/member/login";
		}
	}
}
