package com.QuesTyme.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuesTyme.entity.OneOnOne;
import com.QuesTyme.entity.Slot;
import com.QuesTyme.entity.User;
import com.QuesTyme.exceptions.ApiException;
import com.QuesTyme.repository.UserRepository;
import com.QuesTyme.service.SlotService;

@RestController
@RequestMapping("/slot")
public class SlotController {

	@Autowired
	private SlotService slotService;

	@Autowired
	private UserRepository userRepository;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-slots")
	public ResponseEntity<String> createOneOnOneSlots(@RequestBody OneOnOne oneOnOne) {
		String response = slotService.createOneOnOneSlots(oneOnOne);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('STUDENT')")
	@PostMapping("/bookslot/{slotId}/user/{userId}")
	public ResponseEntity<String> BookSlot(@PathVariable Integer slotId, @PathVariable Integer userId ) {

		String response = slotService.Bookslot(userId, slotId);
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteslot/{slotId}")
	public ResponseEntity<String> DeleteSlot(@PathVariable Integer slotId) {
		String response = slotService.DeleteSlot(slotId);
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	//For admin side
	@GetMapping("/get-all-slot/{adminId}/{date}")
	public ResponseEntity<List<Slot>> getAllTodaySlots(@PathVariable Integer adminId ,  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		 return new ResponseEntity<List<Slot>>(slotService.getAllSlotByAdminId(date, adminId), HttpStatus.OK);
	}

	@GetMapping("/get-book-slot/{adminId}")
	public ResponseEntity<List<Slot>> getBookedTodayAndUpcomingSlots(@PathVariable Integer adminId){
		 return new ResponseEntity<List<Slot>>(slotService.getBookedSlotByAdminId(adminId), HttpStatus.OK);
	}

	@GetMapping("/get-slot/{adminId}")
	public ResponseEntity<List<Slot>> getslotsByAdminId(@PathVariable Integer adminId){
		 return new ResponseEntity<List<Slot>>(slotService.getSlotByAdminId(adminId), HttpStatus.OK);
	}
	
	@GetMapping("/get-All-slots/{adminId}")
	public ResponseEntity<List<Slot>> getAllslotsByAdminId(@PathVariable Integer adminId){
		 return new ResponseEntity<List<Slot>>(slotService.getAllSlotByAdminId(adminId), HttpStatus.OK);
	}

	@GetMapping("/get-slot-dates/{adminId}")
	public ResponseEntity<HashMap<String, List>> getslotsDateByAdminId(@PathVariable Integer adminId){
		HashMap<String, List> response = new HashMap<>();
		List<User> userList = new ArrayList<>();
		Optional<User> user = userRepository.findById(adminId);
		if(user.isPresent()) {
			userList.add(user.get());
			List<LocalDate> dates = slotService.getAdminAndDates(adminId);
			response.put("Admin", userList);
			response.put("dates", dates);
			return new ResponseEntity<HashMap<String, List>>(response, HttpStatus.OK);
		}
		else {
			throw new ApiException("User not Found with this ID");
		}

	}

	//user side
	 @GetMapping("/get-unbooked-slot/{adminId}/{date}")
	public ResponseEntity<List<Slot>> getSlotByDateAndAdminId(@PathVariable Integer adminId ,  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

		return new ResponseEntity<List<Slot>>(slotService.getUnbookedSlotByDateAndAdminId(date, adminId), HttpStatus.OK);
	}


	@GetMapping("/get-all-types")
	public ResponseEntity<Map<String, List<String>>> getSlottype() {

		Map<String, List<String>> response = slotService.getAllAvailableTypes();

		return new ResponseEntity<Map<String, List<String>>>(response, HttpStatus.OK);
	}

	@GetMapping("/get-all-available-admin/{type}")
	public ResponseEntity<Map<String, List<User>>> getAdminAvailable(@PathVariable String type) {
		Map<String, List<User>> response = slotService.getAllAdminByType(type);
		List<User> userList = response.get("Instructors");
		
		if(userList.size()>0) {
		return new ResponseEntity<Map<String, List<User>>>(response, HttpStatus.OK);
		}
		else {
			 throw new ApiException("No Slots Available With this Type");
		}
	}


	@GetMapping("/get-analytics/{adminId}")
	public ResponseEntity<Map<String, Object>> getAnalyticsByAdminId(@PathVariable Integer adminId){
	
		
		return new ResponseEntity<Map<String, Object>>(slotService.getSlotAnalyticsByAdminId(adminId), HttpStatus.OK);
		
	}
	
	@GetMapping("/get-analytics/")
	public ResponseEntity<Map<String, Object>> getAnalytics(){
	
		
		return new ResponseEntity<Map<String, Object>>(slotService.getSlotAnalytics(), HttpStatus.OK);
		
	}

}
