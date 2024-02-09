package com.mikich.beststore.controllers;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mikich.beststore.models.Product;
import com.mikich.beststore.models.ProductDto;
import com.mikich.beststore.services.ProductsRepository;

import jakarta.validation.Valid;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/products")
public class ProductsController {
	
	@Autowired
	private ProductsRepository repo;
	
	@GetMapping({"", "/"})
	public String showProductsList(Model model) {
		List<Product> products = repo.findAll();
		model.addAttribute("products",products);
		return "products/index";
	}
    
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productdto = new ProductDto();
		model.addAttribute("productDto",productdto);
		return "products/CreateProduct";
	}
	
	@PostMapping({"/create"})
	public String createProduct(
		@Valid @ModelAttribute ProductDto productDto,
		BindingResult result
		) {
		
		if (result.hasErrors()) {
			return "products/CreateProduct";
		}
		
		Date createdAt = new Date();
		
		Product product = new Product();
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setCategory(productDto.getCategory());
		product.setPrice(productDto.getPrice() + "");
		product.setDescription(productDto.getDescription());
		product.setCreatedAt(createdAt);
		
		repo.save(product);
		
		return "redirect:/products";
	}
	
	@GetMapping({"/edit"})
	public String showEditPage(Model model, @RequestParam int id) {
		
		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product",product);
			
			ProductDto productDto = new ProductDto();
			productDto.setName(product.getName());
			productDto.setBrand(product.getBrand());
			productDto.setCategory(product.getCategory());
			productDto.setPrice(Float.parseFloat(product.getPrice()));
			productDto.setDescription(product.getDescription());
			
			model.addAttribute("productDto", productDto);
		}
		catch(Exception ex) {
			System.out.println("Exception " + ex.getMessage());
			return "redirect:/products";
		}
		
		return "products/EditProduct";
		
	}
	
	@PostMapping({"/edit"})
	public String updateProduct(Model model, @RequestParam int id,
			@Valid @ModelAttribute ProductDto productDto, 
			BindingResult result) {
		
		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product",product);
			
			if(result.hasErrors()) {
				return "products/EditProduct";
			}
			
			
			
			product.setName(productDto.getName());
			product.setBrand(productDto.getBrand());
			product.setCategory(productDto.getCategory());
			product.setPrice(productDto.getPrice() + "");
			product.setDescription(productDto.getDescription());
			
			repo.save(product);
			
		}
		catch(Exception ex) {
			System.out.println("Exception " + ex.getMessage());
			return "redirect:/products";
		}
		
		return "redirect:/products";
	}
	
	@GetMapping({"/delete"})
	public String deleteProduct(@RequestParam int id) {
		try {
			Product product = repo.findById(id).get();
			repo.delete(product);
		}
		catch(Exception ex) {
			System.out.println("Exception " + ex.getMessage());
		}
		return "redirect:/products";
	}
}
