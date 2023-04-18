package com.QuesTyme.service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuesTyme.dto.AvailabilityDto;
import com.QuesTyme.dto.RecurringMeetingDto;
import com.QuesTyme.dto.SlotTiming;
import com.QuesTyme.entity.Availability;
import com.QuesTyme.entity.RecurringMeeting;
import com.QuesTyme.entity.Slot;
import com.QuesTyme.exceptions.ApiException;
import com.QuesTyme.repository.AvailabilityRepo;
import com.QuesTyme.repository.RecurringMeetingRepo;
import com.QuesTyme.repository.SlotRepo;

@Service
public class RecurringMeetingServices {

	@Autowired
	private RecurringMeetingRepo recurringMeetingRepo;

	@Autowired
	private AvailabilityRepo availabilityRepo;

	@Autowired
	private SlotRepo slotRepo;

	public String createMeeting(RecurringMeetingDto recurringMeeting) {

		RecurringMeeting createdMeeting = new RecurringMeeting();

		createdMeeting.setTitle(recurringMeeting.getTitle());
		createdMeeting.setAdminId(recurringMeeting.getAdminId());
		createdMeeting.setMeetingLink(recurringMeeting.getMeetingLink());
		createdMeeting.setInstruction(recurringMeeting.getInstruction());
		createdMeeting.setDuration(recurringMeeting.getDuration());
		createdMeeting.setType(recurringMeeting.getType());

//		System.out.println(recurringMeeting.getAvailabilities().get(1).getSlotTiming().getEndTime());

		List<Availability> availList = new ArrayList<>();

		int len = recurringMeeting.getAvailabilities().size();

		for (int i = 0; i < len; i++) {

			AvailabilityDto avlDto = recurringMeeting.getAvailabilities().get(i);

			// for SlotTiming List
			int l = avlDto.getSlotTiming().size();
			for (int j = 0; j < l; j++) {
				Availability availability = new Availability();

				availability.setDay(avlDto.getDay());
				availability.setStartTime(avlDto.getSlotTiming().get(j).getStartTime());
				availability.setEndTime(avlDto.getSlotTiming().get(j).getEndTime());
				availability.setRecurringMeeting(createdMeeting);

				availList.add(availability);
			}

		}

		createdMeeting.setAvailabilities(availList);

		recurringMeetingRepo.save(createdMeeting);

		return "Recurring meeting created Succesfully";

	}

	public List<RecurringMeeting> getMeetingList() {

		List<RecurringMeeting> recurringMeetings = recurringMeetingRepo.findAll();

		return recurringMeetings;

	}

	public List<RecurringMeeting> getMeetingListByAdminID(int adminId) {

		List<RecurringMeeting> recurringMeetings = recurringMeetingRepo.findByAdminId(adminId);

		return recurringMeetings;

	}

	public List<Availability> getAvailabilityByAdmin(int adminId) {

		List<Availability> availabilities = recurringMeetingRepo.findAvailabilitiesByAdminId(adminId);

		return availabilities;
	}

	public List<Map<String, Object>> getDailyRecurringSchedule(String day) {
		List<Object[]> Data = recurringMeetingRepo.getRecurringData(day);

		List<Map<String, Object>> jsonResults = new ArrayList<>();

		for (Object[] row : Data) {
			Integer recurringId = (Integer) row[0];
			Integer adminId = (Integer) row[1];
			Integer duration = (Integer) row[2];
			String Instruction = (String) row[3];
			String meetingLink = (String) row[4];
			String title = (String) row[5];
			String type = (String) row[6];
			String day1 = (String) row[8];
			Time StartTime = (Time) row[10];
			Time endTime = (Time) row[9];

			Map<String, Object> jsonRow = new HashMap<>();
			jsonRow.put("recurringId", recurringId);
			jsonRow.put("adminId", adminId);
			jsonRow.put("duration", duration);
			jsonRow.put("Instruction", Instruction);
			jsonRow.put("meetingLink", meetingLink);
			jsonRow.put("title", title);
			jsonRow.put("type", type);
			jsonRow.put("day", day1);
			jsonRow.put("startTime", StartTime);
			jsonRow.put("endTime", endTime);

			jsonResults.add(jsonRow);

		}
		return jsonResults;
	}


	public String updateRedurringMeeting(RecurringMeetingDto recurringMeeting, Integer RecurringId) {

		RecurringMeeting recurringMeeting2 = recurringMeetingRepo.findById(RecurringId)
				.orElseThrow(() -> new ApiException("Recurring event not Found"));

		List<Integer> availabilityIds = availabilityRepo.getAvailabilityByRecurringId(RecurringId);

		availabilityRepo.deleteAllById(availabilityIds);
		List<Integer> slotIDs = slotRepo.getSlotByRecurringId(RecurringId);

		slotRepo.deleteAllById(slotIDs);
		recurringMeeting2.setTitle(recurringMeeting.getTitle());
		recurringMeeting2.setAdminId(recurringMeeting.getAdminId());
		recurringMeeting2.setMeetingLink(recurringMeeting.getMeetingLink());
		recurringMeeting2.setInstruction(recurringMeeting.getInstruction());
		recurringMeeting2.setDuration(recurringMeeting.getDuration());
		recurringMeeting2.setType(recurringMeeting.getType());

		List<Availability> availList = new ArrayList<>();

		int len = recurringMeeting.getAvailabilities().size();

		for (int i = 0; i < len; i++) {

			AvailabilityDto avlDto = recurringMeeting.getAvailabilities().get(i);

			// for SlotTiming List
			int l = avlDto.getSlotTiming().size();
			for (int j = 0; j < l; j++) {
				Availability availability = new Availability();

				availability.setDay(avlDto.getDay());
				availability.setStartTime(avlDto.getSlotTiming().get(j).getStartTime());
				availability.setEndTime(avlDto.getSlotTiming().get(j).getEndTime());
				availability.setRecurringMeeting(recurringMeeting2);

				availList.add(availability);
			}

		}

		recurringMeeting2.setAvailabilities(availList);

		recurringMeetingRepo.save(recurringMeeting2);

//		this.createMeeting(recurringMeeting);

		return "Successfully Updated";

	}

	public String DeleteRecurringEvent(Integer RecurringId) {
		RecurringMeeting recurringMeeting2 = recurringMeetingRepo.findById(RecurringId)
				.orElseThrow(() -> new ApiException("Recurring event not Found"));

		recurringMeetingRepo.deleteById(RecurringId);
		return "recurring event Deleted" ;
	}

	public String createRecurringSlots(RecurringMeetingDto recurringMeetingDto) {

		int len = recurringMeetingDto.getAvailabilities().size();

		for (int i = 0; i < len; i++) {

			AvailabilityDto avlDto = recurringMeetingDto.getAvailabilities().get(i);

			List<SlotTiming> slotTime = avlDto.getSlotTiming();

			if (slotTime.size() == 0) {

				throw new ApiException("Timing should be greater than Duration");
			} else {

				List<Slot> slotlist = new ArrayList<>();

				for (int j = 0; j < slotTime.size(); j++) {

					LocalTime startTime = slotTime.get(j).getStartTime();
					LocalTime endTime = slotTime.get(j).getEndTime();

					while (startTime.compareTo(endTime) != 0) {

						Slot makeSlot = new Slot();


						makeSlot.setTitle(recurringMeetingDto.getTitle());
						makeSlot.setInstruction(recurringMeetingDto.getInstruction());
						makeSlot.setMeetingLink(recurringMeetingDto.getMeetingLink());
						makeSlot.setAdminId(recurringMeetingDto.getAdminId());
						makeSlot.setUserId(recurringMeetingDto.getAdminId());
						makeSlot.setType(recurringMeetingDto.getType());
						makeSlot.setType(recurringMeetingDto.getType());
						makeSlot.setStatus('U');

						// edit startTime
						makeSlot.setStartTime(startTime);
						startTime = startTime.plusMinutes(recurringMeetingDto.getDuration());
						makeSlot.setEndTime(startTime);

						slotlist.add(makeSlot);

					}
				}

				slotRepo.saveAll(slotlist);
			}
		}

		return "Slots created Successfully";
 
	}

}