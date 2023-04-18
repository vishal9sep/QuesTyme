package com.QuesTyme.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuesTyme.dto.SlotTiming;
import com.QuesTyme.entity.Interviews;
import com.QuesTyme.entity.OneOnOne;
import com.QuesTyme.entity.Slot;
import com.QuesTyme.entity.User;
import com.QuesTyme.exceptions.ApiException;
import com.QuesTyme.exceptions.ResourceNotFoundException;
import com.QuesTyme.repository.InterviewRepo;
import com.QuesTyme.repository.SlotRepo;
import com.QuesTyme.repository.UserRepository;

@Service
public class SlotService {

	@Autowired
	private SlotRepo slotRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InterviewRepo interviewRepo;

	public String createOneOnOneSlots(OneOnOne oneOnOne) {
		List<SlotTiming> slotTime = oneOnOne.getSlotTime();
		if (slotTime.size() == 0) {
			throw new ApiException("Time is Required !");
		} else {
			List<Slot> slotlist = new ArrayList<>();
			for (int i = 0; i < slotTime.size(); i++) {
				LocalTime startTime = slotTime.get(i).getStartTime();
				LocalTime endTime = slotTime.get(i).getEndTime();

				while (startTime.compareTo(endTime) != 0) {
					Slot makeSlot = new Slot();
					makeSlot.setTitle(oneOnOne.getTitle());
					makeSlot.setInstruction(oneOnOne.getInstruction());
					makeSlot.setMeetingLink(oneOnOne.getMeetingLink());
					makeSlot.setAdminId(oneOnOne.getAdminId());
					makeSlot.setDate(oneOnOne.getDate());
					makeSlot.setType(oneOnOne.getType());
					makeSlot.setStatus('U');

					// edit startTime
					makeSlot.setStartTime(startTime);
					startTime = startTime.plusMinutes(oneOnOne.getDuration());
					makeSlot.setEndTime(startTime);

					slotlist.add(makeSlot);
//					slotRepo.save(makeSlot);

				}
			}

			slotRepo.saveAll(slotlist);
			return "Slots created Successfully";
		}

	}

	public String Bookslot(Integer userId, Integer slotId) {

		String response;
		Slot slot = slotRepo.findById(slotId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", slotId));
		if (slot.getStatus() == 'U') {
			slot.setUserId(userId);
			slot.setStatus('B');
			slotRepo.save(slot);

			Interviews interview = new Interviews();

			interview.setInterviewerId(slot.getAdminId());
			interview.setIntervieweeId(slot.getUserId());
			interview.setStartTime(slot.getStartTime());
			interview.setEndTime(slot.getEndTime());
			interview.setDate(slot.getDate());
			interview.setInstructions(slot.getInstruction());
			
			interview.setMeetingLink(slot.getMeetingLink());
			interview.setTitle(slot.getTitle());
			interview.setMeetingStatus("P");
			interview.setCategory(slot.getType());

			interviewRepo.save(interview);
			response = "Slot Booked";
		} else {
			response = "Slot Already Booked";
		}
		return response;

	}

	public String DeleteSlot(Integer slotId) {

		Slot slot = slotRepo.findById(slotId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", slotId));

		slot.setStatus('D');
		slotRepo.save(slot);

		return "slot deleted";

	}

	// This method provide slots using adminId and today and upcomings slots not
	// previous
	public List<Slot> getAllSlotByAdminId(LocalDate date, Integer adminId) {
		return slotRepo.FindBydateStatusAdminId(date, adminId);
	}

	public List<Slot> getBookedSlotByAdminId(Integer adminId) {
		return slotRepo.FindBydateStatusBAdminId(adminId);
	}

	public List<Slot> getSlotByAdminId(Integer adminId) {
		return slotRepo.FindByAdminId(adminId);
	}
	
	public List<Slot> getAllSlotByAdminId(Integer adminId) {
		return slotRepo.FindByAllSlotsAdminId(adminId);
	}

	public List<LocalDate> getAdminAndDates(Integer adminId) {
		return (List<LocalDate>) slotRepo.findAllDistinctDates(adminId);

	}

	public List<Slot> getUnbookedSlotByDateAndAdminId(LocalDate date, Integer adminId) {
		return slotRepo.FindBydateAndAdminId(date, adminId);

	}

	public Map<String, List<String>> getAllAvailableTypes() {
		Map<String, List<String>> hashMap = new HashMap<>();
		List<String> types = slotRepo.findAllDistincttypes();
		hashMap.put("type", types);
		return hashMap;

	}

	public Map<String, List<User>> getAllAdminByType(String type) {
		Map<String, List<User>> hashMap = new HashMap<>();
		List<Integer> adminIds = slotRepo.findAllAdminByType(type);
		List<User> users = userRepository.findByIds(adminIds);
		hashMap.put("Instructors", users);
		return hashMap;
	}

	public Map<String, Object> getSlotAnalyticsByAdminId(Integer adminId){
		List<Object[]> data =  slotRepo.FindNumberOfSlotsByAdminId(adminId);
		  
		List<Map<String, Object>> jsonResults = new ArrayList<>();
		Long totalSlots=(long) 0;
		
		for (Object[] row : data) {
			Long count = (Long) row[1];
			Character meetingStatus = (Character) row[0];
			totalSlots+=count;
			Map<String, Object> jsonRow = new HashMap<>();
			jsonRow.put("count", count);
			jsonRow.put("meetingStatus", meetingStatus);
			

			jsonResults.add(jsonRow);
		}
		Map<String, Object> jsonResult = new HashMap<>();
		jsonResult.put("totalSlots", totalSlots);
		jsonResult.put("results", jsonResults);
		
		return jsonResult;
	}
	
	public Map<String, Object> getSlotAnalytics(){
		List<Object[]> data =  slotRepo.FindSlotsAnalytics();
		  
		List<Map<String, Object>> jsonResults = new ArrayList<>();
		Long totalSlots=(long) 0;
		
		for (Object[] row : data) {
			Long count = (Long) row[1];
			Character meetingStatus = (Character) row[0];
			totalSlots+=count;
			Map<String, Object> jsonRow = new HashMap<>();
			jsonRow.put("count", count);
			jsonRow.put("meetingStatus", meetingStatus);
			

			jsonResults.add(jsonRow);
		}
		Map<String, Object> jsonResult = new HashMap<>();
		jsonResult.put("totalSlots", totalSlots);
		jsonResult.put("results", jsonResults);
		
		return jsonResult;
	}
}
