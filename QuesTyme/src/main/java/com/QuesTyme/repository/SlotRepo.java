package com.QuesTyme.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.QuesTyme.entity.Slot;

public interface SlotRepo extends JpaRepository<Slot, Integer> {

	@Query("SELECT s FROM Slot s where s.date=:date")
	List<Slot> findByLocalDate(@Param("date") LocalDate date);

	@Query("SELECT e FROM Slot e WHERE e.date =:date AND (e.status='U' OR e.status='B') AND e.adminId =:adminId")
	List<Slot> FindBydateStatusAdminId(@Param("date") LocalDate date, @Param("adminId") Integer adminId);

	@Query("SELECT e FROM Slot e WHERE e.date >= CURRENT_DATE AND e.status='B' AND e.adminId =:adminId")
	List<Slot> FindBydateStatusBAdminId(@Param("adminId") Integer adminId);

	@Query("SELECT e FROM Slot e WHERE e.date >= CURRENT_DATE AND e.adminId =:adminId AND (e.status='U' OR e.status='B')"  )
	List<Slot> FindByAdminId(@Param("adminId") Integer adminId);
	
	@Query("SELECT e FROM Slot e WHERE e.adminId =:adminId AND (e.status='U' OR e.status='B')"  )
	List<Slot> FindByAllSlotsAdminId(@Param("adminId") Integer adminId);

	@Query("SELECT DISTINCT e.date FROM Slot e where e.adminId =:adminId AND e.date >= CURRENT_DATE")
	List<LocalDate> findAllDistinctDates(@Param("adminId") Integer adminId);

	@Query("SELECT e FROM Slot e WHERE e.date =:date AND e.status='U' AND e.adminId =:adminId")
	List<Slot> FindBydateAndAdminId(@Param("date") LocalDate date, @Param("adminId") Integer adminId);

	@Query("SELECT DISTINCT e.type FROM Slot e where  e.date >= CURRENT_DATE")
	List<String> findAllDistincttypes();

	@Query("SELECT DISTINCT e.adminId FROM Slot e where e.type=:type AND  e.date >= CURRENT_DATE")
	List<Integer> findAllAdminByType(@Param("type") String type);
	
	@Query("SELECT  me.status, COUNT(me.status) FROM Slot me WHERE me.date >= CURRENT_DATE AND me.adminId =:adminId  GROUP BY me.status")
	List<Object[]> FindNumberOfSlotsByAdminId(@Param("adminId") Integer adminId);
	
	@Query("SELECT  me.status, COUNT(me.status) FROM Slot me WHERE me.date >= CURRENT_DATE  GROUP BY me.status")
	List<Object[]> FindSlotsAnalytics();
	
	
	@Query(value ="select e.slot_id from questyme.slot e where e.recurring_id=:id and e.status='U'" ,  nativeQuery = true)
	List<Integer> getSlotByRecurringId(@Param("id") Integer RecurringId);
	
	
}
