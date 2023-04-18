package com.QuesTyme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuesTyme.config.Category;

@RestController
@RequestMapping("/api/category")
public class EnumController {
	
	@GetMapping("/")
	public Category[] getTypes() {
		
		return Category.values();
	}

}