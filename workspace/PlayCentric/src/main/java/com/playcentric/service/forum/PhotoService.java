package com.playcentric.service.forum;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.forum.ForumPhoto;
import com.playcentric.model.forum.PhotoRepository;
import com.playcentric.model.forum.Texts;
import com.playcentric.model.forum.TextsRepository;

@Service
public class PhotoService {

	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
	private TextsRepository textsRepository;

	// 尋找單前TextsId圖片
	public ForumPhoto getPhotoDataByTextsId(Integer textsId) {
		Optional<Texts> optional = textsRepository.findById(textsId);
		Texts texts = optional.isPresent()? optional.get():null;
		List<ForumPhoto> photos = photoRepository.findByTexts(texts);
		if (!photos.isEmpty()) {
			return photos.get(0); // 返回第一个匹配的图片对象
		} else {
			return null; // 或者根据业务逻辑返回适当的默认值或抛出异常
		}
	}

	// 新增圖片
	public ForumPhoto insertPhoto(ForumPhoto photo) {
		return photoRepository.save(photo);
	}

	// 以id查詢圖片
	public ForumPhoto findPhotoById(Integer id) {
		Optional<ForumPhoto> optional = photoRepository.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	// 查詢全部圖片
	public List<ForumPhoto> findAllPhotos() {
		return photoRepository.findAll();
	}

	public void deleteByTexts(Texts texts){
		photoRepository.deleteByTexts(texts);
	}
}
