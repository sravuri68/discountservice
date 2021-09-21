package com.sample.project.discountservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sample.project.discountservice.model.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

	@Query(value="select * FROM discount  where item_type = ?1 OR item_cost < ?2 OR item_id = ?3", nativeQuery = true)
	List<Discount> getAllValidDiscounts(String itemType, Double totalCost, Integer itemId);
}
