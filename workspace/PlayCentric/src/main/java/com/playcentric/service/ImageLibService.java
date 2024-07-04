package com.playcentric.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.ImageLib;
import com.playcentric.model.ImageLibRepository;

@Service
public class ImageLibService {
    
    @Autowired
    private ImageLibRepository imageLibRepository;

    public ImageLib findImageById(Integer imgId){
        Optional<ImageLib> optional = imageLibRepository.findById(imgId);
        return optional.isPresent()? optional.get():null;
    }

    public ImageLib saveImage(ImageLib imageLib){
        return imageLibRepository.save(imageLib);
    }
}
