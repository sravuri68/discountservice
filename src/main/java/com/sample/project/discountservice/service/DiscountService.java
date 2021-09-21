package com.sample.project.discountservice.service;

import java.util.List;

import com.sample.project.discountservice.model.Discount;
import com.sample.project.discountservice.model.DiscountResponse;
import com.sample.project.discountservice.model.Item;

public interface DiscountService {

	public Discount saveDiscount(Discount discount);
	
	public void deleteDiscount(Integer discountId);

	public DiscountResponse calculateDiscount(List<Item> items) throws Exception;

}
