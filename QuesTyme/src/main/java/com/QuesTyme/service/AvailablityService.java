package com.QuesTyme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuesTyme.repository.AvailabilityRepo;

@Service
public class AvailablityService {

	@Autowired
	AvailabilityRepo availabilityRepo;

//	public String createAvailability(AvailabilityDto availabilityDto) {
//
//		Availability avl = new Availability();
//		
//		avl.setDay(availabilityDto.getDay());
////		avl.setStartTime(availabilityDto.getSlotTiming().getStartTime());
////		avl.setEndTime(availabilityDto.getSlotTiming().getEndTime());
//
//		availabilityRepo.save(avl);
//
//		return "availability addded succesfuly";
//
//	}

}