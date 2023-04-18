package com.QuesTyme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuesTyme.entity.Availability;
import com.QuesTyme.entity.RecurringMeeting;

@Repository
public interface RecurringMeetingRepo extends JpaRepository<RecurringMeeting, Integer>{
	
	 List<Availability> findAvailabilitiesByRecurringId(Integer recurringId);
	 
	 List<Availability> findAvailabilitiesByAdminId(Integer adminId);
	 
	 @Query("select r from RecurringMeeting r where r.adminId =:adminId")
	 List<RecurringMeeting> findByAdminId(Integer adminId);
	 
	 @Query(value = "SELECT * from questyme.recurring_meeting r, questyme.availability a where r.recurring_id=a.recurring_meeting_id and a.avail_day=:day", nativeQuery = true)
		List<Object[]> getRecurringData(@Param("day") String day);

}