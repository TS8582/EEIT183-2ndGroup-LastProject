package com.playcentric.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.ImageLib;
import com.playcentric.model.ImageLibRepository;

@Service
public class ImageLibService {

	@Autowired
	private ImageLibRepository imageLibRepository;

	public ImageLib findImageById(Integer imgId) {
		Optional<ImageLib> optional = imageLibRepository.findById(imgId);
		return optional.isPresent() ? optional.get() : null;
	}

	public ImageLib saveImage(ImageLib imageLib) {
		return imageLibRepository.save(imageLib);
	}

	// 討論區附加

//	// 尋找當前textsId圖片
//	public ImageLib getImageDataByTextsId(Integer textsId) {
//		List<ImageLib> img = imageLibRepository.findByTextsId(textsId);
//		if (!img.isEmpty()) {
//			return img.get(0); // 返回第一个匹配的图片对象
//		} else {
//			return null; // 或者根据业务逻辑返回适当的默认值或抛出异常
//		}
//	}
	
	// 查詢全部圖片
	public List<ImageLib> findAllImage(){
		return imageLibRepository.findAll();
	}
}
