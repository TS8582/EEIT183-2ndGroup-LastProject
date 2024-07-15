package com.playcentric.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.ImageLib;
import com.playcentric.model.forum.Texts;
import com.playcentric.service.ImageLibService;
import com.playcentric.service.forum.TextsService;

@Controller
@RequestMapping("/imagesLib")
public class ImageLibController {

	@Autowired
	private ImageLibService imageLibService;

	@Autowired
	private TextsService textsService;

	@GetMapping("/image{id}")
	public ResponseEntity<byte[]> getMethodName(@PathVariable Integer id) {
		ImageLib imageLib = imageLibService.findImageById(id);
		byte[] imageFile = imageLib.getImageFile();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);

		return new ResponseEntity<byte[]>(imageFile, headers, HttpStatus.OK);
	}

	@GetMapping("/upload")
	public String uploadImage() {
		return "uploadImage";
	}

	@PostMapping("/upload")
	public String uploadPost(Model model, @RequestParam MultipartFile photoFile) throws IOException {
		ImageLib imageLib = new ImageLib();
		imageLib.setImageFile(photoFile.getBytes());
		imageLibService.saveImage(imageLib);
		model.addAttribute("okMsg", "上傳成功!");
		return "redirect:upload";
	}

	// 討論區使用
//	@GetMapping("/showImage2")
//	public ResponseEntity<byte[]> showImage2(@RequestParam("textsId") Integer textsId) {
//		try {
//			// 根据 textsId 查询对应的图片数据或路径
//			ImageLib imageLib = imageLibService.getImageDataByTextsId(textsId);
//
//			if (imageLib != null && imageLib.getImageFile() != null) {
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.IMAGE_JPEG); // 设置响应内容类型为图片类型
//				return new ResponseEntity<>(imageLib.getImageFile(), headers, HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 找不到对应的图片数据
//			}
//		} catch (Exception e) {
//			// 处理异常情况，比如找不到图片等
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

//	@GetMapping("/upload")
//	public String upload() {
//		return "photos/uploadPage";
//	}
//
//	@PostMapping("/uploadPost")
//	public String uploadPost(@RequestParam("file") MultipartFile[] files, Model model) throws IOException {
//
//		Texts texts = new Texts();
//
//		ArrayList<ImageLib> arrayList = new ArrayList<>();
//
//		for (MultipartFile oneFile : files) {
//			ImageLib imageLib = new ImageLib();
//			imageLib.setImageFile(oneFile.getBytes());
//			imageLib.setTexts(texts); // 多 set 1
//
//			arrayList.add(imageLib);
//		}
//		texts.setImgLib(arrayList);
//
//		textsService.insert(texts);
//
//		model.addAttribute("okMsg", "上傳成功");
//
//		return "uploadPage";
//
//	}

	@GetMapping("/downloadImage")
	public ResponseEntity<byte[]> downloadImage(@RequestParam Integer id) {
		ImageLib imageLib = imageLibService.findImageById(id);

		byte[] imgFile = imageLib.getImageFile();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		// body, headers , http status code
		return new ResponseEntity<byte[]>(imgFile, headers, HttpStatus.OK);
	}

//	@GetMapping("/showImage")
//	public String showImage(Model mode) {
//		List<ImageLib> allImage = imageLibService.findAllImage();
//		mode.addAttribute("allImage", allImage);
//		return "showPage";
//	}

}
