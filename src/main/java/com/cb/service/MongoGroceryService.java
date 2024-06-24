package com.cb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb.aop.Loggable;
import com.cb.document.GroceryItem;
import com.cb.repository.ItemRepository;

@Service
public class MongoGroceryService {

	@Autowired
	ItemRepository groceryItemRepo;

	// READ
	// 1. Show all the data
	@Loggable
	public List<GroceryItem> showAllGroceryItems() {

		return groceryItemRepo.findAll();
	}

	// 2. Get item by name
	public GroceryItem getGroceryItemByName(String name) {
		System.out.println("Getting item by name: " + name);
		GroceryItem item = groceryItemRepo.findItemByName(name);
		System.out.println(getItemDetails(item));
		return item;
	}

	// 3. Get name and quantity of a all items of a particular category
	public List<GroceryItem> getItemsByCategory(String category) {
		System.out.println("Getting items for the category " + category);
		List<GroceryItem> list = groceryItemRepo.findAll(category);

		list.forEach(item -> System.out.println("Name: " + item.getName() + ", Quantity: " + item.getQuantity()));
		return list;
	}

	// 4. Get count of documents in the collection
	public long findCountOfGroceryItems() {
		long count = groceryItemRepo.count();
		System.out.println("Number of documents in the collection: " + count);
		return count;
	}

	public String getItemDetails(GroceryItem item) {

		System.out.println("Item Name: " + item.getName() + ", \nQuantity: " + item.getQuantity()
				+ ", \nItem Category: " + item.getCategory());

		return "";
	}

	public void updateCategoryName(String category) {

		// Change to this new value
		String newCategory = "munchies";

		// Find all the items with the category snacks
		List<GroceryItem> list = groceryItemRepo.findAll(category);

		list.forEach(item -> {
			// Update the category in each document
			item.setCategory(newCategory);
		});

		// Save all the items in database
		List<GroceryItem> itemsUpdated = groceryItemRepo.saveAll(list);

		if (itemsUpdated != null)
			System.out.println("Successfully updated " + itemsUpdated.size() + " items.");
	}

	@Transactional
	public List<GroceryItem> updateCategoryName(String oldCategory, String newCategory) {
		// Change to this new value
//		String newCategory = "munchies";

		// Find all the items with the category snacks
		List<GroceryItem> list = groceryItemRepo.findAll(oldCategory);

		list.forEach(item -> {
			// Update the category in each document
			item.setCategory(newCategory);
		});

		// Save all the items in database
		List<GroceryItem> itemsUpdated = groceryItemRepo.saveAll(list);

		if (itemsUpdated != null)
			System.out.println("Successfully updated " + itemsUpdated.size() + " items.");
		return itemsUpdated;
	}

}
