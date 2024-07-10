package com.playcentric.model.playfellow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.playcentric.model.ImageLib;


public interface ImageLibPfmemberAssociationRepository extends JpaRepository<ImageLibPfmemberAssociation, Integer> {

    List<ImageLibPfmemberAssociation> findByPlayFellowMember(PlayFellowMember playFellowMember);
    
    void deleteByImageLib(ImageLib imageLib);
}
