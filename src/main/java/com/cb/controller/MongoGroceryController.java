package com.cb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cb.aop.Loggable;
import com.cb.document.GroceryItem;
import com.cb.service.MongoGroceryService;

@RestController
@RequestMapping("/api/grocery")
public class MongoGroceryController {

	@Autowired
	MongoGroceryService groceryService;

	@GetMapping("/items")
	public List<GroceryItem> showAllGroceryItems() {
		return groceryService.showAllGroceryItems();
	}

	@GetMapping("/item")
	public ResponseEntity<GroceryItem> getGroceryItemByName(@RequestParam String name) {
		return new ResponseEntity<>(groceryService.getGroceryItemByName(name), HttpStatus.OK);
	}

	@GetMapping("/items/category")
	public List<GroceryItem> getItemsByCategory(@RequestParam String category) {
		return groceryService.getItemsByCategory(category);
	}

	@GetMapping("/items/count")
	public Long findCountOfGroceryItems() {
		return groceryService.findCountOfGroceryItems();
	}

	@PutMapping("/items/category")
	public ResponseEntity<List<GroceryItem>> updateCategoryName(@RequestParam String oldCategory,
			@RequestParam String newCategory) {
		return new ResponseEntity<>(groceryService.updateCategoryName(oldCategory, newCategory), HttpStatus.OK);
	}

	@Loggable
	public void showAllGroceryItems1() {
		showAllGroceryItems();
		getGroceryItemByName("Bonny Cheese Crackers Plain");
		getItemsByCategory("snacks");
		findCountOfGroceryItems();
		updateCategoryName("snacks", "munchies");
	}

	@Scheduled(fixedDelay = 30000)
	public void name() {
		System.out.println("MongoGroceryController.name()");
		showAllGroceryItems();
	}

}
