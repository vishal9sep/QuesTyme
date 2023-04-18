package com.QuesTyme.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDto{
	private String interviewerEmail;
    private String intervieweeEmail;
    private LocalTime startTime;
    private LocalTime endTime;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private String category;
    private String instructions;
    private String title;
    private String meetingLink;
    private String batch;
}
