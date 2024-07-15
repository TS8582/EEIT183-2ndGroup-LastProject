package com.playcentric.controller.forum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.forum.ForumPhoto;
import com.playcentric.model.forum.Texts;
import com.playcentric.service.forum.PhotoService;
import com.playcentric.service.forum.TextsService;

@Controller
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private TextsService textsService;

	@GetMapping("/photo/showPhoto2")
	public ResponseEntity<byte[]> showPhoto2(@RequestParam("textsId") Integer textsId) {
		try {
			// 根据 postId 查询对应的图片数据或路径
			ForumPhoto forumPhoto = photoService.getPhotoDataByTextsId(textsId);

			if (forumPhoto != null && forumPhoto.getPhotoFile() != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_JPEG); // 设置响应内容类型为图片类型
				return new ResponseEntity<>(forumPhoto.getPhotoFile(), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 找不到对应的图片数据
			}
		} catch (Exception e) {
			// 处理异常情况，比如找不到图片等
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/photo/upload")
	public String upload() {
		return "photos/uploadPage";
	}

	@PostMapping("/photo/uploadPost")
	public String uploadPost(@RequestParam("file") MultipartFile[] files, Model model) throws IOException {

		Texts texts = new Texts();

		ArrayList<ForumPhoto> arrayList = new ArrayList<>();

		for (MultipartFile oneFile : files) {
			ForumPhoto forumPhoto = new ForumPhoto();
			forumPhoto.setPhotoFile(oneFile.getBytes());
			forumPhoto.setTexts(texts); // 多 set 1

			arrayList.add(forumPhoto);
		}
		texts.setForumPhoto(arrayList);

		textsService.insert(texts);

		model.addAttribute("okMsg", "上傳成功");

		return "photo/uploadPage";

	}

	@GetMapping("/photo/download2")
	public ResponseEntity<byte[]> downloadPhoto(@RequestParam Integer id) {
		ForumPhoto photo = photoService.findPhotoById(id);

		byte[] photoFile = photo.getPhotoFile();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		// body, headers , http status code
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}

	@GetMapping("/photo/showPhotos")
	public String showPhotos(Model mode) {
		List<ForumPhoto> allPhotos = photoService.findAllPhotos();
		mode.addAttribute("allPhotos", allPhotos);
		return "photo/showPage";
	}

}
