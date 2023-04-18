package com.QuesTyme.Mapper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuesTyme.dto.InterviewDto;
import com.QuesTyme.dto.InterviewResponseDto;
import com.QuesTyme.entity.Interviews;
import com.QuesTyme.entity.User;
import com.QuesTyme.service.UserService;

@Component
public class InterviewMapper {
	
    public static Interviews mapToInterview(InterviewDto dto, User interviewee, User interviewer) {
        Interviews interview = new Interviews();
        interview.setIntervieweeId(interviewee.getId());
        interview.setInterviewerId(interviewer.getId());
        interview.setTitle(dto.getTitle());
        interview.setCategory(dto.getCategory());
        interview.setInstructions(dto.getInstructions());
        interview.setStartTime(dto.getStartTime());
        interview.setEndTime(dto.getEndTime());
        interview.setDate(dto.getDate());
        interview.setMeetingLink(dto.getMeetingLink());
        interview.setBatch(dto.getBatch());
        return interview;
    }
    public static InterviewResponseDto mapToInterviewResponseDto(Interviews interview, User interviewer, User interviewee) {
        InterviewResponseDto dto = new InterviewResponseDto();
        dto.setInterviewId(interview.getInterviewId());
        dto.setIntervieweeName(interviewee.getName());
        dto.setInterviewerName(interviewer.getName());
        dto.setTitle(interview.getTitle());
        dto.setCategory(interview.getCategory());
        dto.setInstructions(interview.getInstructions());
        dto.setStartTime(interview.getStartTime());
        dto.setEndTime(interview.getEndTime());
        dto.setDate(interview.getDate());
        dto.setMeetingLink(interview.getMeetingLink());
        dto.setBatch(interview.getBatch());
        dto.setMeetingStatus(interview.getMeetingStatus());
        dto.setStudentNote(interview.getStudentsNotes());
        dto.setAdminFeedback(interview.getAdminFeedback());
        dto.setIntervieweeEmail(interviewee.getEmail());
        dto.setInterviewerEmail(interviewer.getEmail());
        return dto;
    }
    
}