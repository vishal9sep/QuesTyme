package com.QuesTyme.config;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.QuesTyme.entity.Slot;
import com.QuesTyme.repository.SlotRepo;
import com.QuesTyme.service.RecurringMeetingServices;

@Component
public class DailySlotCreaterScheduler {

	@Autowired
	private RecurringMeetingServices recurringMeetingServices;

	@Autowired
	private SlotRepo slotRepo;

	@Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
//		@Scheduled(cron = "0 20 15 * * *")

	public void recurringScheduler() {

		String dayOfWeekString = LocalDate.now().getDayOfWeek().toString();
		List<Map<String, Object>> dailySchedules = recurringMeetingServices.getDailyRecurringSchedule(dayOfWeekString);

		for (int i = 0; i < dailySchedules.size(); i++) {
			List<Slot> Allslots = new ArrayList<>();
			Map<String, Object> map = dailySchedules.get(i);

			Time sqlStartTime = (Time) map.get("startTime");
			Time sqlEndTime = (Time) map.get("endTime");

			LocalTime startTime = sqlStartTime.toLocalTime();
			LocalTime endTime = sqlEndTime.toLocalTime();
			Integer duration = (Integer) map.get("duration");
			while (startTime.compareTo(endTime) != 0) {
				Slot makeSlot = new Slot();
				makeSlot.setTitle((String) map.get("title"));
				makeSlot.setInstruction((String) map.get("Instruction"));
				makeSlot.setMeetingLink((String) map.get("meetingLink"));
				makeSlot.setDate(LocalDate.now().plusWeeks(1));
				makeSlot.setType((String) map.get("type"));
				makeSlot.setAdminId((Integer) map.get("adminId"));
				makeSlot.setStatus('U');
				makeSlot.setStartTime(startTime);
				startTime = startTime.plusMinutes(duration);
				makeSlot.setEndTime(startTime);
				makeSlot.setRecurringId((Integer) map.get("recurringId"));
//						Allslots.add(makeSlot);
				slotRepo.save(makeSlot);

			}
//				 slotRepo.saveAll(Allslots);

		}
//			 slotRepo.saveAll(Allslots);

	}

}