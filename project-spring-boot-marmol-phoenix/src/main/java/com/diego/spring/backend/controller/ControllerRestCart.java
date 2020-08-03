package com.diego.spring.backend.controller;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.spring.backend.model.Product;
import com.diego.spring.backend.model.ProductByUserCart;
import com.diego.spring.backend.model.UserProgram;
import com.diego.spring.backend.service.GeneralService;

@RestController
@RequestMapping("/api/cartuser")
@CrossOrigin(origins = {"*"})
public class ControllerRestCart {
	
	@Autowired
	GeneralService service;
	
	/**
	 * Get all products from the cart
	 * @return {@link Set}
	 */
	@PostMapping("/cart-products")
	public Set<Product> getProductByCartUser(@RequestBody UserProgram user) {
		return service.getProductsByUserCart(user.getUserName());
	}
	
	/**
	 * Get all products without amounts
	 * @return {@link Set}
	 */
	@PostMapping("/cart-products-length")
	public List<ProductByUserCart> getProductsWithoutAmounts(@RequestBody UserProgram user) {
		return service.getProductsWithoutAmounts(user.getUserName());
	}
	
	/**
	 * Delete  product from cart
	 * @return {@link ResponseEntity}
	 * @param Map<String username, String id >
	 */
	@PostMapping("/delete-product")
	public ResponseEntity<?> deleteProductFromCart(@RequestBody Map<String, String> res) {
		return service.deleteProductByUserCart(res.get("userName"), Long.parseLong(res.get("id")));
	}
	
	/**
	 * Delete All product from cart by productId
	 * @return {@link ResponseEntity}
	 * @param Map<String username, String id >
	 */
	@PostMapping("/delete-productsid")
	public ResponseEntity<?> deleteAllProductsByProductIdFromCart(@RequestBody Map<String, String> res) {
		return service.deleteAllProductsByProductId(res.get("userName"), Long.parseLong(res.get("id")));
	}
	
	/**
	 * Delete products from cart
	 * @return {@link ResponseEntity}
	 * @param username
	 */
	@PostMapping("/delete-products")
	public ResponseEntity<?> deleteProductsFromCart(@RequestBody UserProgram user) {
		return service.deleteAllProductsCartByUserName(user.getUserName());
	}
	
	/**
	 * Add products to cart
	 * @return {@link ResponseEntity}
	 * @param Map<String username, String id >
	 */
	@PostMapping("/add-product")
	public ResponseEntity<?> addProductsToCart(@RequestBody Map<String, String> res) {
		ProductByUserCart p = new ProductByUserCart();
		p.setProductId(Long.parseLong(res.get("id")));
		p.setUserName(res.get("userName"));
		return service.addProductByUserCart(p);
	}
	
	/**
	 * Update the userName on list cart when the userName was update
	 * @return {@link ResponseEntity}
	 * @param Map<String userName new, String userName old >
	 */
	@PostMapping("/update-new-username")
	public ResponseEntity<?> updateNewUserNameOnLists(@RequestBody Map<String, String> res) {
		return service.updateUserNameNewOnProductCartList(res.get("userNameNew"), res.get("userNameOld"));
	}
	

}
