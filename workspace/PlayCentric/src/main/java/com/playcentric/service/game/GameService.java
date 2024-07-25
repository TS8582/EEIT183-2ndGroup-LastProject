package com.playcentric.service.game;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameDiscount;
import com.playcentric.model.game.primary.GameDiscountRepository;
import com.playcentric.model.game.primary.GameRepository;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gRepo;
	
	@Autowired
	private GameDiscountRepository gdRepo;
	
	//商店展示的遊戲
	public Page<Game> findShowInStore(Pageable pgb) {
		return gRepo.findByIsShow(true,pgb);
	}
	
	//展示遊戲回傳list
	public List<Game> findByIsShow() {
		return gRepo.findByIsShow(true);
	}
	
	//價格找遊戲回傳list
	public List<Game> findByPriceList(Integer minPrice,Integer maxPrice) {
		return gRepo.findByPriceBetween(minPrice, maxPrice);
	}
	
	//價格找遊戲
	public Page<Game> findByPriceBetween(Integer minPrice,Integer maxPrice,Pageable pgb) {
		return gRepo.findByPriceBetween(minPrice, maxPrice, pgb);
	}
	
	//新增遊戲
	public Game save(Game game) {
		return gRepo.save(game); 
	}
	
	//id找遊戲
	public Game findById(Integer id) {
		return gRepo.findById(id).get();
	}
	
	//取全部遊戲
	public List<Game> findAll() {
		return gRepo.findAll();
	}
	
	//更新以id找到的遊戲
	public Game update(Integer id,Game game) {
		Game origin = findById(id);
		origin.setDescription(game.getDescription());
		origin.setDeveloper(game.getDeveloper());
		origin.setGameDiscounts(game.getGameDiscounts());
		origin.setGameFile(game.getGameFile());
		origin.setGameName(game.getGameName());
		origin.setGameTypeLibs(game.getGameTypeLibs());
		origin.setImageLibs(game.getImageLibs());
		origin.setIsShow(game.getIsShow());
		origin.setPrice(game.getPrice());
		origin.setPublisher(game.getPublisher());
		origin.setReleaseAt(game.getReleaseAt());
		return gRepo.save(origin);
	}
	
	//刪除以id找到的遊戲
	public void delete(Integer id) {
		Game game = findById(id);
		gRepo.delete(game);
	}
	
	//找遊戲目前的優惠
	public GameDiscount findNowDiscount(Integer gameId) {
		return gdRepo.findNowDiscount(LocalDateTime.now(), gameId);
	}
	
	//設定折扣與折扣價
	public Integer setRateAndDiscountPrice(Game game) {
		GameDiscount nowDiscount = findNowDiscount(game.getGameId());
		if (nowDiscount != null) {
			Double oldRate = Double.parseDouble(nowDiscount.getDiscountRate().toString());
			int rate = (int) (oldRate * 100);
			game.setRate(rate);
			Integer discountedPrice = game.getPrice() * game.getRate() / 100;
			game.setDiscountedPrice(discountedPrice);
			return discountedPrice;
		}
		game.setDiscountedPrice(game.getPrice());
		return game.getPrice();
	}
	
	
}
