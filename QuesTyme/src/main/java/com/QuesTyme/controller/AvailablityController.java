package com.QuesTyme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuesTyme.service.AvailablityService;

@RestController
@RequestMapping("/availablity")
public class AvailablityController {
	
	@Autowired
	AvailablityService availablityService;
	
//	@PostMapping("/add")
//	public ResponseEntity<String> createAvailability(@RequestBody AvailabilityDto availabilityDto){
//		
//		String response = availablityService.createAvailability(availabilityDto);
//		
//		return new ResponseEntity<String>(response, HttpStatus.OK);
//		
//	}

}