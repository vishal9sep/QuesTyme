package com.QuesTyme.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.QuesTyme.dto.RecurringMeetingDto;
import com.QuesTyme.entity.Availability;
import com.QuesTyme.entity.RecurringMeeting;
import com.QuesTyme.repository.RecurringMeetingRepo;
import com.QuesTyme.repository.RecurringMeetingRepo;
import com.QuesTyme.service.RecurringMeetingServices;

@RestController
@RequestMapping("/recurring")
public class RecurringMeetingController {

	@Autowired
	private RecurringMeetingServices recurringMeetingServices;


	@Autowired
	private RecurringMeetingRepo recurringMeetingRepo;


	@PostMapping("/createRecMeet")
	public ResponseEntity<String> creatMeeting(@RequestBody RecurringMeetingDto recurringMeeting) {

		String response = this.recurringMeetingServices.createMeeting(recurringMeeting);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}


	@PostMapping("/creatRecurringSlots")
	public ResponseEntity<String> createRecurringSlots(@RequestBody RecurringMeetingDto recurringMeetingDto) {

		String response = this.recurringMeetingServices.createRecurringSlots(recurringMeetingDto);

		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@GetMapping("/getList")
	public ResponseEntity<List<RecurringMeeting>> getRecurringMeetings() {

		List<RecurringMeeting> recurringMeetings = recurringMeetingServices.getMeetingList();

		return new ResponseEntity<List<RecurringMeeting>>(recurringMeetings, HttpStatus.OK);

	}

	@GetMapping("/getListByAdminId")
	public ResponseEntity<List<RecurringMeeting>> getRecurringMeetingsByAdminID(@RequestParam Integer adminId) {

		List<RecurringMeeting> recurringMeetings = recurringMeetingServices.getMeetingListByAdminID(adminId);

		return new ResponseEntity<List<RecurringMeeting>>(recurringMeetings, HttpStatus.OK);

	}

	@GetMapping("/getAvailabilityByAdmin")
	public ResponseEntity<List<Availability>> getAvailabilityByAdmin(@RequestParam Integer adminId) {

		List<Availability> availabilities = recurringMeetingServices.getAvailabilityByAdmin(adminId);

		return new ResponseEntity<List<Availability>>(availabilities, HttpStatus.OK);
	}

	@GetMapping("/get-data")
	public ResponseEntity<List<Map<String, Object>>> getdata(@RequestParam String day) {

		List<Map<String, Object>> availabilities = recurringMeetingServices.getDailyRecurringSchedule(day);

		return new ResponseEntity<List<Map<String, Object>>>(availabilities, HttpStatus.OK);

	}

	@DeleteMapping("/{recurringId}")
	public ResponseEntity<String> deleteRecurringMeeting(@PathVariable Integer recurringId) {
		return new ResponseEntity<String>(recurringMeetingServices.DeleteRecurringEvent(recurringId), HttpStatus.OK);
	}

	@PutMapping("/update/{recurringId}")
	public ResponseEntity<String> updateRecurringMeeting(@RequestBody RecurringMeetingDto recurringMeeting,
			@PathVariable Integer recurringId) {
		return new ResponseEntity<String>(
				recurringMeetingServices.updateRedurringMeeting(recurringMeeting, recurringId), HttpStatus.OK);
	}



}