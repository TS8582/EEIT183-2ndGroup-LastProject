package com.playcentric.model.game.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RechargeRepository extends JpaRepository<Recharge,Integer> {

    Recharge findByMemId(Integer memId, PageRequest pageable);

    Page<Recharge> findByMemIdAndStatus(Integer memId, Short status,Pageable pageable);

}
