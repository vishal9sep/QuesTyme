package com.QuesTyme.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringMeetingDto {

	private int adminId;

	private String title;

	private String meetingLink;

	private String instruction;

	private int duration;

	private String type;

	private List<AvailabilityDto> availabilities;

}
