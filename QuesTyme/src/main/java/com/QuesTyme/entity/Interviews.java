package com.QuesTyme.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interviews {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "interview_id")
	private int interviewId;
	@Column(name = "interviewer_id")
	private int interviewerId;
	@Column(name = "interviewee_id")
	private int intervieweeId;
	@Column(name = "start_time")
	private LocalTime startTime;
	@Column(name = "end_time")
	private LocalTime endTime;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Column(name = "interview_date")
	private LocalDate date;
	@Column(name = "students_notes")
	private String studentsNotes;
	@Column(name = "admin_feedback")
	private String adminFeedback;
	private String category;
	private String instructions;
	private String title;

	@Column(name = "meeting_link")
	private String meetingLink;
	@Column(name = "meeting_status")
	private String meetingStatus = "P";
	private String batch;

	private boolean reminderSent;

	public boolean isReminderSent() {
		return reminderSent;
	}

	public void setReminderSent(boolean reminderSent) {
		this.reminderSent = reminderSent;
	}

}