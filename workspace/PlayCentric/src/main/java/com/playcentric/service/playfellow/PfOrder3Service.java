package com.playcentric.service.playfellow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.playfellow.PfOrder3;
import com.playcentric.model.playfellow.PfOrder3Repository;



@Service
public class PfOrder3Service {
	
	@Autowired
	PfOrder3Repository pfOrder3Repository;
	
	public PfOrder3 savePfOrder3(PfOrder3 pfOrder3) {
		return pfOrder3Repository.save(pfOrder3);
	}
}
