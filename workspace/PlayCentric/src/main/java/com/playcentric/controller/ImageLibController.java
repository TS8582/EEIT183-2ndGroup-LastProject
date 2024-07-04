package com.playcentric.controller;

import java.io.IOException;

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
import com.playcentric.service.ImageLibService;


@Controller
@RequestMapping("/imagesLib")
public class ImageLibController {
    
    @Autowired
    private ImageLibService imageLibService;

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
	public String uploadPost(Model model,
			@RequestParam MultipartFile photoFile) throws IOException {
		ImageLib imageLib = new ImageLib();
		imageLib.setImageFile(photoFile.getBytes());
		imageLibService.saveImage(imageLib);
		model.addAttribute("okMsg", "上傳成功!");
		return "redirect:upload";
	}
}
