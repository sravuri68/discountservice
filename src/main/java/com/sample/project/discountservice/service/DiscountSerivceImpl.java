package com.sample.project.discountservice.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.project.discountservice.model.Discount;
import com.sample.project.discountservice.model.DiscountResponse;
import com.sample.project.discountservice.model.Item;
import com.sample.project.discountservice.repository.DiscountRepository;

@Service
public class DiscountSerivceImpl implements DiscountService {

	@Autowired
	private DiscountRepository discountRepository;

	@Override
	public Discount saveDiscount(Discount discount) {
		return discountRepository.save(discount);
	}

	@Override
	public void deleteDiscount(Integer discountId) {
		discountRepository.deleteById(discountId);

	}

	@Override
	public DiscountResponse calculateDiscount(List<Item> items) throws Exception {
		Double totalCost = null;
		String itemType = null;
		Integer itemId = null;
		List<Discount> discountList = null;

		if (items.size() > 0) {

			itemType = items.stream().filter(x -> x.getType().equals("CLOTHES")).map(Item::getType).findFirst()
					.orElse(null);

			totalCost = items.stream().filter(x -> x.getCost() > 100).map(Item::getCost).findFirst().orElse(null);

			itemId = items.stream().filter(x -> x.getQuantity() > 1).map(Item::getId).findFirst().orElse(null);

			discountList = discountRepository.getAllValidDiscounts(itemType, totalCost, itemId);

			Discount bestDiscount = getBestDiscount(discountList);
			double discountedCost = 0;

			for (Item item : items) {
				switch (bestDiscount.getPercentage()) {
				case 20:
					if (bestDiscount.getItemId().equals(item.getId())) {
						discountedCost = discountedCost + getTotalAmount(bestDiscount.getPercentage(), true,
								item.getCost(), item.getQuantity());
					} else {
						discountedCost = discountedCost + item.getCost();
					}
					break;
				case 15:
					if (bestDiscount.getItemCost() < item.getCost()) {
						discountedCost = discountedCost
								+ getTotalAmount(bestDiscount.getPercentage(), false, item.getCost(), null);
					} else {
						discountedCost = discountedCost + item.getCost();
					}
					break;
				case 10:
					if (bestDiscount.getItemType().equals(item.getType())) {
						discountedCost = discountedCost
								+ getTotalAmount(bestDiscount.getPercentage(), false, item.getCost(), null);
					} else {
						discountedCost = discountedCost + item.getCost();
					}
					break;
				default:
					discountedCost = discountedCost + item.getCost();
				}
			}
			DiscountResponse discountResponse = calculateBestDiscount(bestDiscount.getCode(), discountedCost);
			return discountResponse;

		} else {
			throw new Exception("Invalid Request");
		}

	}

	private double getTotalAmount(Integer percentage, boolean isQuantityMoreThanOne, Double cost, Integer quantity) {
		if (isQuantityMoreThanOne) {
			double totalCost = cost * quantity;
			return (totalCost - (totalCost * percentage) / 100);
		} else {
			return (cost - (cost * percentage) / 100);
		}

	}

	private DiscountResponse calculateBestDiscount(String discountCode, Double discountedCost) {
		DiscountResponse discountResponse = new DiscountResponse();
		discountResponse.setDiscountId(discountCode);
		discountResponse.setTotalCost(discountedCost);
		return discountResponse;
	}

	private Discount getBestDiscount(List<Discount> discountList) {
		return discountList.stream().max(Comparator.comparingInt(Discount::getPercentage)).get();

	}

}
