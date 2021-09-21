package com.sample.project.discountservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.project.discountservice.model.Discount;
import com.sample.project.discountservice.model.DiscountResponse;
import com.sample.project.discountservice.model.Item;
import com.sample.project.discountservice.service.DiscountService;

@RestController
@RequestMapping("/api")
public class DiscountController {

	@Autowired
	private DiscountService discountServiceImpl;

	@GetMapping("/calculateDiscount")
	public ResponseEntity<DiscountResponse> getDiscount(@RequestBody List<Item> items) {
		try {
			DiscountResponse discountResponse = discountServiceImpl.calculateDiscount(items);
			return new ResponseEntity<>(discountResponse, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/addDiscount")
	public ResponseEntity<Discount> addDiscount(@RequestBody Discount discount) {
		try {
			Discount savedDiscount = discountServiceImpl.saveDiscount(discount);
			return new ResponseEntity<>(savedDiscount, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteDiscount/{discountId}")
	public ResponseEntity<HttpStatus> deleteDiscount(@PathVariable("discountId") Integer discountId) {
		try {
			discountServiceImpl.deleteDiscount(discountId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
