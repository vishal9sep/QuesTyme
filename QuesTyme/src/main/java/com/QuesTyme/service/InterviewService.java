package com.QuesTyme.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.QuesTyme.Mapper.InterviewMapper;
import com.QuesTyme.dto.InterviewDto;
import com.QuesTyme.dto.InterviewResponseDto;
import com.QuesTyme.entity.Interviews;
import com.QuesTyme.entity.User;
import com.QuesTyme.exceptions.InterviewException;
import com.QuesTyme.repository.InterviewRepo;
import com.QuesTyme.repository.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Service
public class InterviewService {
	@Autowired
	private InterviewRepo interviewrepo;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userServiceimpl;
	@Autowired
	private EmailService emailService;

	/**
	 * 
	 * This method retrieves all the users from the UserRepository and stores them
	 * in a Map with the email as the key and the User object as the value.
	 * 
	 * @return A Map<String, User> containing all the users from the UserRepository.
	 */
	public Map<String, User> userMap() {
		Map<String, User> map = new HashMap<>();
		userRepository.findAll().forEach(user -> map.put(user.getEmail(), user));
		return map;
	}

	/**
	 * 
	 * This method creates an interview with the provided information in the
	 * InterviewDto object.
	 * 
	 * @param interviewdto An InterviewDto object containing the information about
	 *                     the interview.
	 * 
	 * @return An Interviews object containing the information of the created
	 *         interview.
	 * 
	 * @throws InterviewException If the interviewee is already booked for the given
	 *                            date and time.
	 */
	public Interviews createInterview(InterviewDto interviewdto) throws InterviewException {
		Map<String, User> user = userMap();
		User interviewee = user.get(interviewdto.getIntervieweeEmail());
		User interviewer = user.get(interviewdto.getInterviewerEmail());
		List<Interviews> interviews = interviewrepo.findByInterview(interviewee.getId(), interviewdto.getDate(),
				interviewdto.getStartTime(), interviewdto.getEndTime(), interviewer.getId());
		if (!interviews.isEmpty()) {
			throw new InterviewException("Interviewee is already booked for the given date and time");
		}
		Interviews interview = InterviewMapper.mapToInterview(interviewdto, interviewee, interviewer);
		interview = interviewrepo.save(interview);

		interviewrepo.save(interview);
		// Send an email to the interviewer to notify them about the scheduled
		// interview.
		String interviewerSubject = "Interview Scheduled Successfully";
		String interviewerBody = "Dear " + interviewer.getName() + ",\n\nYour interview with " + interviewee.getName()
				+ " has been scheduled for " + interview.getDate() + " from " + interview.getStartTime() + " to "
				+ interview.getEndTime() + ".\n\nPlease use the following link to join the meeting: "
				+ interview.getMeetingLink() + "\n\n Or you can visit the interview platform"
				+ "\n\nBest regards,\nThe Interview Team";
		emailService.sendEmail(interviewdto.getInterviewerEmail(), interviewerSubject, interviewerBody);
		// Send an email to the interviewee to notify them about the scheduled
		// interview.
		String intervieweeSubject = "Interview Scheduled Successfully";
		String intervieweeBody = "Dear " + interviewee.getName() + ",\n\nYour interview with " + interviewer.getName()
				+ " has been scheduled for " + interview.getDate() + " from " + interview.getStartTime() + " to "
				+ interview.getEndTime() + ".\n\nPlease use the following link to join the meeting: "
				+ interview.getMeetingLink() + "\n\n Or you can visit the interview platform"
				+ "\n\nBest regards,\nThe Interview Team";
		emailService.sendEmail(interviewdto.getIntervieweeEmail(), intervieweeSubject, intervieweeBody);
		return interview;
	}

	/**
	 * Reads a CSV file containing interview data, creates Interview objects from
	 * the data, and saves them to the database.
	 *
	 * @param file The CSV file containing interview data.
	 * @return A message indicating whether the interviews were created successfully
	 *         or not.
	 * @throws InterviewException If there is an error while creating or updating an
	 *                            interview.
	 * @throws IOException        If there is an error while reading the CSV file.
	 * @throws CsvException       If there is an error while parsing the CSV data.
	 */
	public String createInterviewsFromCsv(MultipartFile file) throws InterviewException, IOException, CsvException {
		CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
		List<String[]> rows = csvReader.readAll();
		csvReader.close();
		List<Interviews> interviews = new ArrayList<>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		Map<String, User> user = userMap();
		for (int i = 1; i < rows.size(); i++) {
			String[] row = rows.get(i);
			Interviews interview = new Interviews();
			User interviewer = user.get(row[0]);
			User interviewee = user.get(row[1]);
			if (interviewer != null) {
				interview.setInterviewerId(interviewer.getId());
			}
			if (interviewee != null) {
				interview.setIntervieweeId(interviewee.getId());
			}
			interview.setStartTime(LocalTime.parse(row[2], timeFormatter));
			interview.setEndTime(LocalTime.parse(row[3], timeFormatter));
			interview.setDate(LocalDate.parse(row[4], dateFormatter));
			interview.setCategory(row[5]);
			interview.setInstructions(row[6]);
			interview.setTitle(row[7]);
			interview.setMeetingLink(row[8]);
			interview.setMeetingStatus(row[9]);
			interview.setBatch(row[10]);

			List<Interviews> existingInterviews = interviewrepo.findByInterview(interview.getIntervieweeId(),
					interview.getDate(), interview.getStartTime(), interview.getEndTime(),
					interview.getInterviewerId());
			if (!existingInterviews.isEmpty()) {
				System.out.println("Interviewee " + interviewee.getEmail()
						+ " is already booked for the given date and time, skipping row " + i);
				continue;
			}
			interviews.add(interview);

			String interviewerSubject = "Interview Scheduled Successfully";
			String interviewerBody = "Dear " + interviewer.getName() + ",\n\nYour interview with "
					+ interviewee.getName() + " has been scheduled for " + interview.getDate() + " from "
					+ interview.getStartTime() + " to " + interview.getEndTime()
					+ ".\n\nPlease use the following link to join the meeting: " + interview.getMeetingLink()
					+ "\n\n Or you can visit the interview platform" + "\n\nBest regards,\nThe Interview Team";
			emailService.sendEmail(row[1], interviewerSubject, interviewerBody);

			String intervieweeSubject = "Interview Scheduled Successfully";
			String intervieweeBody = "Dear " + interviewee.getName() + ",\n\nYour interview with "
					+ interviewer.getName() + " has been scheduled for " + interview.getDate() + " from "
					+ interview.getStartTime() + " to " + interview.getEndTime()
					+ ".\n\nPlease use the following link to join the meeting: " + interview.getMeetingLink()
					+ "\n\n Or you can visit the interview platform" + "\n\nBest regards,\nThe Interview Team";
			emailService.sendEmail(row[0], intervieweeSubject, intervieweeBody);

		}
		interviewrepo.saveAll(interviews);

		return "Interview Created Successfully";
	}

	/**
	 * Updates an existing interview with the given ID using the data from the
	 * provided InterviewDto object.
	 *
	 * @param interviewdto The InterviewDto object containing the updated interview
	 *                     data.
	 * @param id           The ID of the interview to update.
	 * @return The updated Interviews object.
	 * @throws InterviewException If there is an error while creating or updating an
	 *                            interview.
	 */
	public Interviews updateInterview(InterviewDto interviewdto, int id) throws InterviewException {
		Map<String, User> user = userMap();
		User interviewee = user.get(interviewdto.getIntervieweeEmail());
		User interviewer = user.get(interviewdto.getInterviewerEmail());
		Interviews interview = interviewrepo.findByInterviewId(id);
		if (interview == null) {
			throw new InterviewException("Interview with id " + id + " not found");
		}
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime interviewDateTime = interviewdto.getDate().atTime(interviewdto.getStartTime());
		LocalDateTime interviewEndDateTime = interviewdto.getDate().atTime(interviewdto.getEndTime());
		if (currentDateTime.isAfter(interviewDateTime)) {
			throw new InterviewException("Cannot update interview, the interview has already started");
		}
		if (currentDateTime.isAfter(interviewEndDateTime)) {
			throw new InterviewException("Cannot update interview, the interview has already ended");
		}
		List<Interviews> conflictingInterviews = interviewrepo.findByInterview(interviewee.getId(),
				interviewdto.getDate(), interviewdto.getStartTime(), interviewdto.getEndTime(), interviewer.getId());
		conflictingInterviews.removeIf(i -> i.getInterviewId() == id);
		if (!conflictingInterviews.isEmpty()) {
			throw new InterviewException("Interviewee is already booked for the given date and time");
		}

		interview.setInterviewerId(interviewer.getId());
		interview.setIntervieweeId(interviewee.getId());
		interview.setStartTime(interviewdto.getStartTime());
		interview.setEndTime(interviewdto.getEndTime());
		interview.setDate(interviewdto.getDate());
		interview.setCategory(interviewdto.getCategory());
		interview.setInstructions(interviewdto.getInstructions());
		interview.setTitle(interviewdto.getTitle());
		interview.setMeetingLink(interviewdto.getMeetingLink());
		interview.setBatch(interviewdto.getBatch());
		interviewrepo.save(interview);

		String interviewerSubject = "Interview Updated Successfully";
		String interviewerBody = "Dear " + interviewer.getName() + ",\n\nYour interview with " + interviewee.getName()
				+ " has been updated for " + interview.getDate() + " from " + interview.getStartTime() + " to "
				+ interview.getEndTime() + ".\n\nPlease use the following link to join the meeting: "
				+ interview.getMeetingLink() + "\n\n Or you can visit the interview platform"
				+ "\n\nBest regards,\nThe Interview Team";
		emailService.sendEmail(interviewdto.getInterviewerEmail(), interviewerSubject, interviewerBody);

		String intervieweeSubject = "Interview Updated Successfully";
		String intervieweeBody = "Dear " + interviewee.getName() + ",\n\nYour interview with " + interviewer.getName()
				+ " has been updated for " + interview.getDate() + " from " + interview.getStartTime() + " to "
				+ interview.getEndTime() + ".\n\nPlease use the following link to join the meeting: "
				+ interview.getMeetingLink() + "\n\n Or you can visit the interview platform"
				+ "\n\nBest regards,\nThe Interview Team";
		emailService.sendEmail(interviewdto.getIntervieweeEmail(), intervieweeSubject, intervieweeBody);
		return interview;
	}

	/**
	 * Cancels an interview with the specified interviewId and updates the meeting
	 * status to "C".
	 * 
	 * Sends email notifications to both the interviewer and the interviewee with
	 * the updated interview details.
	 * 
	 * @param interviewId the ID of the interview to cancel
	 * 
	 * @return the updated Interviews object with the meeting status set to "C"
	 * 
	 * @throws InterviewException if no interview is found with the specified
	 *                            interviewId
	 */
	public Interviews cancelInterview(int interviewId) throws InterviewException {
		Interviews interview = interviewrepo.findByInterviewId(interviewId);

		if (interview == null) {
			throw new InterviewException("Interview with id " + interviewId + " not found");
		}
		Map<Integer, User> user = userServiceimpl.userMap();
		User interviewee = user.get(interview.getIntervieweeId());
		User interviewer = user.get(interview.getInterviewerId());
		LocalDateTime startDateTime = LocalDateTime.of(interview.getDate(), interview.getStartTime());
		LocalDateTime endDateTime = LocalDateTime.of(interview.getDate(), interview.getEndTime());
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (currentDateTime.isAfter(startDateTime)) {
			throw new InterviewException("Cannot update interview, the interview has already started");
		}
		if (currentDateTime.isAfter(endDateTime)) {
			throw new InterviewException("Cannot update interview, the interview has already ended");
		}
		interview.setMeetingStatus("C");

		String interviewerSubject = "Interview Updated Successfully";
		String interviewerBody = "Dear " + interviewer.getName() + ",\n\nYour interview with " + interviewee.getName()
				+ " has been updated for " + interview.getDate() + " from " + interview.getStartTime() + " to "
				+ interview.getEndTime() + ".\n\nPlease use the following link to join the meeting: "
				+ interview.getMeetingLink() + "\n\n Or you can visit the interview platform"
				+ "\n\nBest regards,\nThe Interview Team";
		emailService.sendEmail(interviewer.getEmail(), interviewerSubject, interviewerBody);

		String intervieweeSubject = "Interview Updated Successfully";
		String intervieweeBody = "Dear " + interviewee.getName() + ",\n\nYour interview with " + interviewer.getName()
				+ " has been updated for " + interview.getDate() + " from " + interview.getStartTime() + " to "
				+ interview.getEndTime() + ".\n\nPlease use the following link to join the meeting: "
				+ interview.getMeetingLink() + "\n\n Or you can visit the interview platform"
				+ "\n\nBest regards,\nThe Interview Team";
		emailService.sendEmail(interviewee.getEmail(), intervieweeSubject, intervieweeBody);
		return interviewrepo.save(interview);
	}

	/**
	 * 
	 * Updates the status of an interview meeting based on the given parameters.
	 * 
	 * @param interviewId the id of the interview to update
	 * @param status      the new status to set for the interview meeting
	 * @param userId      the id of the user initiating the status update
	 * @return the updated interview meeting
	 * @throws InterviewException if the interview with the given id is not found,
	 *                            the status is invalid, or the current date and
	 *                            time is before the start time or after the end
	 *                            time of the interview meeting
	 */
	@Transactional
	public Interviews updateMeetingStatus(int interviewId, String status, int userId) throws InterviewException {
		Interviews interview = interviewrepo.findByInterviewId(interviewId);

		LocalDateTime startDateTime = LocalDateTime.of(interview.getDate(), interview.getStartTime());
		LocalDateTime endDateTime = LocalDateTime.of(interview.getDate(), interview.getEndTime());
		LocalDateTime currentDateTime = LocalDateTime.now();

		if (interview == null) {
			throw new InterviewException("Interview with id " + interviewId + " not found");
		}

		if (interview != null && status.equals("S")) {
			if (currentDateTime.isEqual(startDateTime.minusMinutes(5))|| currentDateTime.isAfter(startDateTime.minusMinutes(5))&& (currentDateTime.isBefore(startDateTime) || currentDateTime.isEqual(startDateTime))) {
				if (interview.getInterviewerId() == userId && interview.getMeetingStatus().equals("SS")) {
					interview.setMeetingStatus("S");
				} else if (interview.getInterviewerId() == userId) {
					interview.setMeetingStatus("IS");
				} else if (interview.getIntervieweeId() == userId && interview.getMeetingStatus().equals("IS")) {
					interview.setMeetingStatus("S");
				} else if (interview.getIntervieweeId() == userId) {
					interview.setMeetingStatus("SS");
				}
			} else {
				throw new InterviewException("Start date time has not yet arrived");
			}
		} else if (interview != null && status.equals("E")) {
			if (endDateTime.isBefore(currentDateTime)) {
				if (interview.getInterviewerId() == userId && interview.getMeetingStatus().equals("SE")
						&& interview.getAdminFeedback() != null) {
					interview.setMeetingStatus("E");
				} else if (interview.getInterviewerId() == userId && interview.getAdminFeedback() != null) {
					interview.setMeetingStatus("IE");
				} else if (interview.getIntervieweeId() == userId && interview.getMeetingStatus().equals("IE")) {
					interview.setMeetingStatus("E");
				} else if (interview.getIntervieweeId() == userId) {
					interview.setMeetingStatus("SE");
				}
			} else {
				throw new InterviewException("End date time has not yet arrived");
			}
		} else {
			throw new InterviewException("Invalid meeting status");
		}

		return interviewrepo.save(interview);
	}

	/**
	 * Get all the upcoming and past interview list for a user
	 * 
	 * @param userId id of the user
	 * @param isPast type of api is it past or not
	 * @return the list of interview
	 * @throws InterviewException
	 */
	public List<InterviewResponseDto> getInterviewsByUser(int userId, boolean isPast) throws InterviewException {
		List<Interviews> interviews;
		LocalDate date = LocalDate.now();
		if (isPast) {
			interviews = interviewrepo.findPastInterviewsByUser(userId, date);
		} else {
			interviews = interviewrepo.findUpcomingInterviewsByUser(userId, date);
		}
		if (interviews == null) {
			throw new InterviewException("No interviews for this user ID: " + userId);
		}
		List<InterviewResponseDto> response = new ArrayList();
		Map<Integer, User> user = userServiceimpl.userMap();
		for (Interviews interview : interviews) {
			User interviewee = user.get(interview.getIntervieweeId());
			User interviewer = user.get(interview.getInterviewerId());
			InterviewResponseDto interviewresponse = InterviewMapper.mapToInterviewResponseDto(interview, interviewer,
					interviewee);
			response.add(interviewresponse);
		}
		return response;
	}

	/**
	 * 
	 * @param interviewId id of the interview that we need details
	 * @return the JSON response of the interview
	 * @throws InterviewException
	 */
	public InterviewResponseDto getInterviewsById(int interviewId) throws InterviewException {
		Interviews interview = interviewrepo.findByInterviewId(interviewId);
		Map<Integer, User> user = userServiceimpl.userMap();
		if (interview == null) {
			throw new InterviewException("No inerview for this interview" + interviewId);
		}
		User interviewee = user.get(interview.getIntervieweeId());
		User interviewer = user.get(interview.getInterviewerId());
		InterviewResponseDto interviewresponse = InterviewMapper.mapToInterviewResponseDto(interview, interviewer,
				interviewee);
		return interviewresponse;
	}

	/**
	 * 
	 * Returns a map containing the count of interviews for each meeting status,
	 * along with the total number of interviews.
	 * 
	 * @return a map containing the count of interviews for each meeting status,
	 *         along with the total number of interviews
	 * @throws InterviewException if there is an error retrieving the interview data
	 */
	public Map<String, Object> countInterviewsByMeetingStatus() throws InterviewException {
		List<Object[]> results = interviewrepo.countByMeetingStatus();
		long totalInterviews = interviewrepo.count();

		List<Map<String, Object>> jsonResults = new ArrayList<>();

		for (Object[] row : results) {
			BigInteger count = (BigInteger) row[0];
			String meetingStatus = (String) row[1];

			Map<String, Object> jsonRow = new HashMap<>();
			jsonRow.put("count", count);
			jsonRow.put("meetingStatus", meetingStatus);

			jsonResults.add(jsonRow);
		}

		Map<String, Object> jsonResult = new HashMap<>();
		jsonResult.put("totalInterviews", totalInterviews);
		jsonResult.put("results", jsonResults);

		return jsonResult;
	}

	/**
	 * 
	 * Returns a map containing the count of interviews for each meeting status in
	 * the specified batch, along with the total number of interviews in the batch.
	 * 
	 * @param batchName the name of the batch to filter the interviews by
	 * @return a map containing the count of interviews for each meeting status in
	 *         the specified batch, along with the total number of interviews in the
	 *         batch
	 * @throws InterviewException if there is an error retrieving the interview data
	 */
	public Map<String, Object> countInterviewsByMeetingStatusAndBatch(String batchName) throws InterviewException {
		List<Object[]> results = interviewrepo.countByMeetingStatusAndBatch(batchName);
		List<Map<String, Object>> jsonResults = new ArrayList<>();
		long totalInterviews = interviewrepo.countByBatch(batchName);
		for (Object[] row : results) {
			BigInteger count = (BigInteger) row[0];
			String meetingStatus = (String) row[1];

			Map<String, Object> jsonRow = new HashMap<>();
			jsonRow.put("count", count.intValue());
			jsonRow.put("meetingStatus", meetingStatus);
			jsonRow.put("batch", batchName);

			jsonResults.add(jsonRow);
		}
		Map<String, Object> jsonResult = new HashMap<>();
		jsonResult.put("totalInterviews", totalInterviews);
		jsonResult.put("results", jsonResults);

		return jsonResult;
	}

	/**
	 * 
	 * Returns a list of InterviewResponseDto objects filtered by batch and meeting
	 * status.
	 * 
	 * @param batch         the batch name to filter by
	 * @param meetingStatus the meeting status to filter by
	 * @return a list of InterviewResponseDto objects filtered by batch and meeting
	 *         status
	 * @throws InterviewException if there is an error retrieving the interview data
	 */
	public List<InterviewResponseDto> filterInterviews(String batch, String meetingStatus) throws InterviewException {
		List<Interviews> interviews = interviewrepo.findAllByFilters(meetingStatus, batch);
		Map<Integer, User> users = userServiceimpl.userMap();
		List<InterviewResponseDto> response = new ArrayList();
		for (Interviews interview : interviews) {
			User interviewee = users.get(interview.getIntervieweeId());
			User interviewer = users.get(interview.getInterviewerId());
			InterviewResponseDto interviewresponse = InterviewMapper.mapToInterviewResponseDto(interview, interviewer,
					interviewee);
			response.add(interviewresponse);
		}
		return response;
	}

	/**
	 * 
	 * Updates the student notes of an interview with the given id.
	 * 
	 * @param id             the id of the interview to update
	 * @param noteOrFeedback the new student notes or admin feedback to set for the
	 *                       interview
	 * @throws InterviewException if an interview with the given id is not found
	 */

	public void updateStudentNoteOrAdminFeedback(int id, String noteOrFeedback, int userId) throws InterviewException {
		Interviews optionalInterview = interviewrepo.findByInterviewId(id);
		if (optionalInterview != null && (optionalInterview.getMeetingStatus().equals("S")
				|| optionalInterview.getMeetingStatus().equals("SE")
				|| optionalInterview.getMeetingStatus().equals("IE"))) {
			Interviews interview = optionalInterview;
			if (interview.getIntervieweeId() == userId) {
				interview.setStudentsNotes(noteOrFeedback);
				interviewrepo.save(interview);
			} else if (interview.getInterviewerId() == userId) {
				interview.setAdminFeedback(noteOrFeedback);
				interviewrepo.save(interview);
			} else {
				throw new InterviewException("Unauthorized");
			}

		} else {
			throw new InterviewException("Interview with id " + id + " not found");
		}
	}

}