package com.QuesTyme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuesTyme.entity.Availability;

@Repository
public interface AvailabilityRepo extends JpaRepository<Availability, Integer>{
	
//	List<Availability> findByRecurringMeeting_id(Integer recurringMeeting_Id);

	@Query(value ="Select e.id from questyme.availability e where e.recurring_meeting_id=:id ;  " ,  nativeQuery = true)
	 List<Integer> getAvailabilityByRecurringId(@Param("id") Integer recurringId);
	
}