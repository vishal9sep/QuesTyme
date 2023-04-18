package com.QuesTyme.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuesTyme.entity.Interviews;

@Repository
public interface InterviewRepo extends JpaRepository<Interviews, Integer> {
	/**
	 * Finds interviews for a specific interviewee and interviewer on a given date
	 * and time range.
	 *
	 * @param intervieweeId the ID of the interviewee
	 * @param date          the date of the interview
	 * @param startTime     the start time of the interview
	 * @param endTime       the end time of the interview
	 * @param interviewerId the ID of the interviewer
	 * @return a list of Interviews that match the specified criteria
	 */
	@Query("SELECT i FROM Interviews i WHERE (i.intervieweeId = :intervieweeId OR i.interviewerId = :interviewerId) AND i.date = :date AND ((i.startTime >= :startTime AND i.startTime < :endTime) OR (i.endTime > :startTime AND i.endTime <= :endTime))")
	List<Interviews> findByInterview(int intervieweeId, LocalDate date, LocalTime startTime, LocalTime endTime,
			int interviewerId);

	/**
	 * Finds past interviews for a specific user.
	 *
	 * @param userId the ID of the user
	 * @param date   the current date
	 * @return a list of past Interviews for the specified user
	 */
	@Query("SELECT i FROM Interviews i WHERE (i.interviewerId = :userId OR i.intervieweeId = :userId) AND i.date <:date order by i.date,i.startTime")
	List<Interviews> findPastInterviewsByUser(@Param("userId") int userId, @Param("date") LocalDate date);

	/**
	 * Finds upcoming interviews for a specific user.
	 *
	 * @param userId the ID of the user
	 * @param date   the current date
	 * @return a list of upcoming Interviews for the specified user
	 */
	@Query("SELECT i FROM Interviews i WHERE (i.interviewerId = :userId OR i.intervieweeId = :userId) AND i.date >=:date order by i.date,i.startTime")
	List<Interviews> findUpcomingInterviewsByUser(@Param("userId") int userId, @Param("date") LocalDate date);

	/**
	 * Finds an interview by its ID.
	 *
	 * @param interviewId the ID of the interview
	 * @return the Interview with the specified ID
	 */
	Interviews findByInterviewId(int interviewId);

	/**
	 * Finds all Interviews that match the specified meeting status and batch.
	 *
	 * @param meetingStatus the meeting status to filter by
	 * @param batch         the batch to filter by
	 * @return a list of Interviews that match the specified criteria
	 */
	@Query(value = "SELECT * FROM interviews  WHERE meeting_status =:meetingStatus AND (batch = :batch OR :batch is null) order by date,start_time", nativeQuery = true)
	List<Interviews> findAllByFilters(@Param("meetingStatus") String meetingStatus, @Param("batch") String batch);

	/**
	 * Counts the number of Interviews with each meeting status.
	 *
	 * @return a list of Object arrays containing the count of Interviews and their
	 *         meeting status
	 */
	@Query(value = "SELECT COUNT(meeting_status), meeting_status FROM interviews GROUP BY meeting_status", nativeQuery = true)
	List<Object[]> countByMeetingStatus();

	/**
	 * 
	 * This method counts the number of interviews with each meeting status for a
	 * given batch.
	 * 
	 * @param batch The batch for which to count the interviews.
	 * @return A list of Object arrays containing the meeting status and the count
	 *         of interviews for that status.
	 */
	@Query(value = "SELECT COUNT(meeting_status), meeting_status FROM interviews WHERE batch = :batch  GROUP BY meeting_status ", nativeQuery = true)
	List<Object[]> countByMeetingStatusAndBatch(@Param("batch") String batch);

	/**
	 * 
	 * This method counts the total number of interviews for a given batch.
	 * 
	 * @param batchName The batch for which to count the interviews.
	 * @return The count of interviews for the specified batch.
	 */
	@Query(value = "SELECT COUNT(*) FROM interviews WHERE batch = :batchName", nativeQuery = true)
	long countByBatch(@Param("batchName") String batchName);

}