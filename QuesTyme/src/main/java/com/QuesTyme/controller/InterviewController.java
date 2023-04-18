package com.QuesTyme.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.QuesTyme.dto.InterviewDto;
import com.QuesTyme.dto.InterviewResponseDto;
import com.QuesTyme.entity.Interviews;
import com.QuesTyme.exceptions.InterviewException;
import com.QuesTyme.service.InterviewService;
import com.opencsv.exceptions.CsvException;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {
	@Autowired
	InterviewService interviewservice;

	/**
	 * 
	 * Creates a new interview.
	 * 
	 * @param interviewdto the DTO for the new interview
	 * @return the newly created interview
	 * @throws InterviewException if there is an error creating the interview
	 */
	@PostMapping("/create")
	public ResponseEntity<Interviews> CreateUser(@RequestBody InterviewDto interviewdto) throws InterviewException {
		Interviews createInterview = interviewservice.createInterview(interviewdto);
		return new ResponseEntity<Interviews>(createInterview, HttpStatus.CREATED);
	}

	/**
	 * 
	 * Creates interviews from a CSV file.
	 * 
	 * @param file the CSV file containing the interviews to be created
	 * @return a response entity indicating whether the operation was successful
	 * @throws InterviewException if there is an error creating the interviews
	 * @throws CsvException       if there is an error reading the CSV file
	 */
	@PostMapping("/csv/create")
	public ResponseEntity<?> createInterviewsFromCsv(@RequestParam("file") MultipartFile file)
			throws InterviewException, CsvException {
		try {
			interviewservice.createInterviewsFromCsv(file);
			return ResponseEntity.ok("Successfully created Bulk Interview by CSV file");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CSV file.");
		}
	}

	/**
	 * 
	 * Updates an existing interview.
	 * 
	 * @param id           the ID of the interview to update
	 * @param interviewdto the DTO containing the new interview details
	 * @return a response entity indicating whether the operation was successful
	 */
	@PutMapping("/{interviewId}/update")
	public ResponseEntity<String> updateInterview(@PathVariable("interviewId") int id,
			@RequestBody InterviewDto interviewdto) {
		try {

			interviewservice.updateInterview(interviewdto, id);
			return ResponseEntity.ok("Interview Update Successfully");
		} catch (InterviewException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * 
	 * Cancels an interview with the specified ID.
	 * 
	 * @param id the ID of the interview to cancel
	 * @return a response entity indicating whether the operation was successful
	 * @throws InterviewException if there is an error cancelling the interview
	 */
	@PutMapping("/{interviewId}/cancel")
	public ResponseEntity<String> deleteInterviewById(@PathVariable("interviewId") int id) throws InterviewException {
		try {
			interviewservice.cancelInterview(id);
			return ResponseEntity.ok("Interview with id " + id + " has been cancelled.");
		} catch (InterviewException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	/**
	 * 
	 * Starts the meeting for the interview with the specified ID and user ID.
	 * 
	 * @param interviewId the ID of the interview to start
	 * @param userId      the ID of the user starting the interview
	 * @return a response entity indicating whether the operation was successful
	 * @throws InterviewException if there is an error starting the interview
	 */
	@PutMapping("/{interviewId}/start/{userId}")
	public ResponseEntity<String> startMeeting(@PathVariable("interviewId") int interviewId,
			@PathVariable("userId") int userId) throws InterviewException {
		try {
			interviewservice.updateMeetingStatus(interviewId, "S", userId);
			return ResponseEntity.ok("Interview with id " + interviewId + " has been started.");
		} catch (InterviewException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

	/**
	 * 
	 * End the meeting for the interview with the specified ID and user ID.
	 * 
	 * @param interviewId the ID of the interview to end
	 * @param userId      the ID of the user ending the interview
	 * @return a response entity indicating whether the operation was successful
	 * @throws InterviewException if there is an error ending the interview
	 */
	@PutMapping("/{interviewId}/end/{userId}")
	public ResponseEntity<String> endMeeting(@PathVariable("interviewId") int interviewId,
			@PathVariable("userId") int userId) throws InterviewException {
		try {
			interviewservice.updateMeetingStatus(interviewId, "E", userId);
			return ResponseEntity.ok("Interview with id " + interviewId + " has been ended.");
		} catch (InterviewException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a list of past interviews for a given user.
	 *
	 * @param userId the ID of the user to retrieve past interviews for
	 * @return a ResponseEntity containing a list of InterviewResponseDto objects
	 *         and an HTTP status code
	 * @throws InterviewException if there is an error retrieving the past
	 *                            interviews
	 */
	@GetMapping("/{userId}/past-interviews")
	public ResponseEntity<List<InterviewResponseDto>> getPastInterviewsByUser(@PathVariable("userId") int userId)
			throws InterviewException {
		List<InterviewResponseDto> interviews = interviewservice.getInterviewsByUser(userId, true);
		return new ResponseEntity<List<InterviewResponseDto>>(interviews, HttpStatus.OK);
	}

	/**
	 * Retrieves a list of upcoming interviews for a given user.
	 *
	 * @param userId the ID of the user to retrieve upcoming interviews for
	 * @return a ResponseEntity containing a list of InterviewResponseDto objects
	 *         and an HTTP status code
	 * @throws InterviewException if there is an error retrieving the upcoming
	 *                            interviews
	 */
	@GetMapping("/{userId}/upcoming-interviews")
	public ResponseEntity<List<InterviewResponseDto>> getUpcomingInterviewsByUser(@PathVariable("userId") int userId)
			throws InterviewException {
		List<InterviewResponseDto> interviews = interviewservice.getInterviewsByUser(userId, false);
		return new ResponseEntity<List<InterviewResponseDto>>(interviews, HttpStatus.OK);
	}

	/**
	 * Retrieves a single interview by ID.
	 *
	 * @param interviewId the ID of the interview to retrieve
	 * @return a ResponseEntity containing an InterviewResponseDto object and an
	 *         HTTP status code
	 * @throws InterviewException if there is an error retrieving the interview
	 */
	@GetMapping("/{interviewId}")
	public ResponseEntity<InterviewResponseDto> getInterviewsById(@PathVariable("interviewId") int interviewId)
			throws InterviewException {
		InterviewResponseDto interview = interviewservice.getInterviewsById(interviewId);
		return new ResponseEntity<InterviewResponseDto>(interview, HttpStatus.OK);
	}

	/**
	 * Returns a map containing the count of interviews grouped by meeting status.
	 * 
	 * @return a map containing the count of interviews grouped by meeting status
	 * @throws InterviewException if there is an error retrieving the count of
	 *                            interviews
	 */
	@GetMapping("/count-by-meeting-status")
	public Map<String, Object> countInterviewsByMeetingStatus() throws InterviewException {

		return interviewservice.countInterviewsByMeetingStatus();

	}

	/**
	 * Returns a map containing the count of interviews grouped by meeting status
	 * for a specified batch.
	 * 
	 * @param batchName the name of the batch to filter by
	 * @return a map containing the count of interviews grouped by meeting status
	 *         for the specified batch
	 * @throws InterviewException if there is an error retrieving the count of
	 *                            interviews
	 */
	@GetMapping("/count-by-meeting-status-by-batch")
	public Map<String, Object> countInterviewsByMeetingStatusAndBatch(@RequestParam("batch") String batchName)
			throws InterviewException {

		return interviewservice.countInterviewsByMeetingStatusAndBatch(batchName);

	}

	/**
	 * Returns a list of interviews filtered by batch name and meeting status.
	 * 
	 * @param batch         the name of the batch to filter by (optional)
	 * @param meetingStatus the meeting status to filter by (required)
	 * @return a list of interviews filtered by batch name and meeting status
	 */
	@GetMapping("/filter")
	public ResponseEntity<List<InterviewResponseDto>> filterInterviews(
			@RequestParam(name = "batch", required = false) String batch,
			@RequestParam("meetingStatus") String meetingStatus) {
		try {
			List<InterviewResponseDto> response = interviewservice.filterInterviews(batch, meetingStatus);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InterviewException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Updates the admin feedback for a specific interview.
	 * 
	 * @param id       the id of the interview to update
	 * @param feedback the updated admin feedback
	 * @param userId   the id of the user making the update
	 * @return a response entity indicating the success or failure of the operation
	 * @throws InterviewException if there is an error updating the admin feedback
	 */
	@PostMapping("/{interviewId}/admin-feedback/{userId}")
	public ResponseEntity<String> updateAdminFeedback(@PathVariable("interviewId") int id, @RequestBody String feedback,
			@PathVariable("userId") int userId) throws InterviewException {
		try {
			interviewservice.updateStudentNoteOrAdminFeedback(id, feedback, userId);
			return ResponseEntity.ok("Admin feedback added successfully");
		} catch (InterviewException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Updates the student note for a specific interview.
	 * 
	 * @param id     the id of the interview to update
	 * @param note   the updated student note
	 * @param userId the id of the user making the update
	 * @return a response entity indicating the success or failure of the operation
	 * @throws InterviewException if there is an error updating the student note
	 */
	@PostMapping("/{interviewId}/student-note/{userId}")
	public ResponseEntity<String> updateStudentNote(@PathVariable("interviewId") int id, @RequestBody String note,
			@PathVariable("userId") int userId) throws InterviewException {
		try {
			interviewservice.updateStudentNoteOrAdminFeedback(id, note, userId);
			return ResponseEntity.ok("Student note added successfully");
		} catch (InterviewException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}