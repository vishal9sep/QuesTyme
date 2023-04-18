package com.QuesTyme.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewResponseDto {
	private int interviewId;
	private String interviewerName;
    private String intervieweeName;
    private LocalTime startTime;
    private LocalTime endTime;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private String category;
    private String instructions;
    private String title;
    private String meetingLink;
    private String batch;
    private String meetingStatus;
    private String studentNote;
    private String adminFeedback;
    private String interviewerEmail;
    private String intervieweeEmail;
}
