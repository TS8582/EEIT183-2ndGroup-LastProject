package com.playcentric.controller.playfellow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.playcentric.model.ImageLib;
import com.playcentric.model.ImageLibRepository;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;
import com.playcentric.model.playfellow.ImageLibPfmemberAssociation;
import com.playcentric.model.playfellow.ImageLibPfmemberAssociationRepository;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.model.playfellow.PlayFellowMemberRepository;
import com.playcentric.service.playfellow.PlayFellowMemberService;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class PlayFellowMemberRestController {

	@Autowired
	PlayFellowMemberService playFellowMemberService;

	@Autowired
	PlayFellowMemberRepository playFellowMemberRepository;

	@Autowired
	ImageLibRepository imageLibRepository;

	@Autowired
	ImageLibPfmemberAssociationRepository imageLibPfmemberAssociationRepository;

	@Autowired
	MemberRepository memberRepository;

	@PostMapping("/playFellow/add") // 按下新增
	public ResponseEntity<Map<String, Object>> addPlayFellowMember(@ModelAttribute("members") Member member,
			@ModelAttribute("playFellowMember") PlayFellowMember playFellowMember) {

		playFellowMember.setMember(member);

		Member existingMember = memberRepository.findById(member.getMemId()).orElse(null);
		if (existingMember != null) {
			existingMember.setGender(member.getGender());
			existingMember.setBirthday(member.getBirthday());
			memberRepository.save(existingMember); // 更新 Member 的 gender 和 birthday
		}

		PlayFellowMember savedPlayFellowMember = playFellowMemberService.addPlayFellowMember(playFellowMember);

		// 把playFellowId的編號丟到前面去
		Map<String, Object> response = new HashMap<>();
		response.put("playFellowId", savedPlayFellowMember.getPlayFellowId());
		response.put("message", "新增成功");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/playFellow/Images/add")
	public String addImageFile(@RequestParam("file") MultipartFile[] file,
			@RequestParam("playFellowId") Integer playFellowId) throws IOException {

		// 找到對應的 playFellowId
		PlayFellowMember playFellowMember = playFellowMemberService.getPlayFellowMemberById(playFellowId);

		// 上傳新圖片 保存關聯
		List<ImageLibPfmemberAssociation> associations = new ArrayList<>();
		for (MultipartFile oneFile : file) {
			ImageLib imageLib = new ImageLib();
			imageLib.setImageFile(oneFile.getBytes());

			ImageLib saveImage = imageLibRepository.save(imageLib);

			ImageLibPfmemberAssociation ipfa = new ImageLibPfmemberAssociation(playFellowMember, saveImage);

			associations.add(ipfa);
		}
		imageLibPfmemberAssociationRepository.saveAll(associations);

		return "上傳成功";
	}

	@GetMapping("/playFellow/Images/{playFellowId}")
	public List<Map<String, Object>> getImagesByPlayFellowId(@PathVariable Integer playFellowId) {
		PlayFellowMember playFellowMember = playFellowMemberService.getPlayFellowMemberById(playFellowId);

		// 從中介表找到 playFellowId 相關的照片序
		List<ImageLibPfmemberAssociation> associations = imageLibPfmemberAssociationRepository
				.findByPlayFellowMember(playFellowMember);

		List<Map<String, Object>> imageUrls = new ArrayList<>(); // 設置 imageUrls 的容器

		// 生成每張圖片的 URL 並添加到 imageUrls 列表中
		for (ImageLibPfmemberAssociation association : associations) {
			Map<String, Object> imageData = new HashMap<>();
			imageData.put("url", "/PlayCentric/playFellow/Images/view/" + association.getImageLib().getImageId());
			imageData.put("id", association.getImageLib().getImageId());
			imageUrls.add(imageData);
		}

		return imageUrls;
	}

	@GetMapping("/playFellow/Images/view/{imageId}")
	public ResponseEntity<byte[]> getImageById(@PathVariable Integer imageId) {
		ImageLib imageLib = imageLibRepository.findById(imageId).orElse(null);
		if (imageLib == null) {
			return ResponseEntity.notFound().build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "image/jpeg");
		return new ResponseEntity<>(imageLib.getImageFile(), headers, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/playFellow/Images/delete/{imageId}")
	public ResponseEntity<String> deleteImageById(@PathVariable Integer imageId) {
		Optional<ImageLib> optImage = imageLibRepository.findById(imageId);
		if (!optImage.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("圖片未找到");
		}

		ImageLib imageLib = optImage.get();

		// 刪除關聯表中的對應關聯
		imageLibPfmemberAssociationRepository.deleteByImageLib(imageLib);

		// 刪除圖片表中的記錄
		imageLibRepository.delete(imageLib);

		return ResponseEntity.ok("圖片已刪除");
	}

	@PostMapping("/playFellow/updateMember/save")
	public String savePlayFellowMemberMsg(@RequestParam("pfnickname") String pfnickname,
			@RequestParam("pfdescription") String pfdescription, @RequestParam("playFellowId") Integer playFellowId,
			@RequestParam("pfstatus") Byte pfstatus) {

		Optional<PlayFellowMember> optPlayFellowMember = playFellowMemberRepository.findById(playFellowId);

		if (optPlayFellowMember.isPresent()) {
			PlayFellowMember playFellowMember = optPlayFellowMember.get();
			playFellowMember.setPfnickname(pfnickname);
			playFellowMember.setPfdescription(pfdescription);
			playFellowMember.setPfstatus(pfstatus);
			playFellowMemberRepository.save(playFellowMember);
		}
		return "";
	}

	@DeleteMapping("/playFellow/deleteMember/{playFellowId}")
	public ResponseEntity<String> deletePlayFellowMember(@PathVariable Integer playFellowId) {
		// 檢查 playFellowId 是否為空
		if (playFellowId != null) {
			// 獲取要刪除的 PlayFellowMember
			PlayFellowMember playFellowMember = playFellowMemberService.getPlayFellowMemberById(playFellowId);

			if (playFellowMember != null) {
				// 查找與之關聯的 ImageLibPfmemberAssociation
				List<ImageLibPfmemberAssociation> associations = imageLibPfmemberAssociationRepository
						.findByPlayFellowMember(playFellowMember);

				// 刪除所有關聯的 ImageLibPfmemberAssociation
				imageLibPfmemberAssociationRepository.deleteAll(associations);

				// 刪除 PlayFellowMember
				playFellowMemberRepository.delete(playFellowMember);

				// 返回成功訊息
				return ResponseEntity.ok("刪除成功");
			} else {
				// 如果 playFellowMember 為空，返回錯誤訊息
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無該伴遊者，請重新登入或洽後台管理人員");
			}
		} else {
			// 如果 playFellowId 為空，返回錯誤訊息
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無效的請求，playFellowId 為空");
		}
	}

	@PostMapping("/reviewPfstatus")
	public String reviewPfstatus(@RequestParam("playFellowId") Integer playFellowId,
			@RequestParam("pfstatus") Byte pfstatus) {
		Optional<PlayFellowMember> optPlayFellowMember = playFellowMemberRepository.findById(playFellowId);
		PlayFellowMember playFellowMember = optPlayFellowMember.get();
		playFellowMember.setPfstatus(pfstatus);
		playFellowMemberRepository.save(playFellowMember);

		return "";
	}

}
