package com.QuesTyme.entity;

import java.time.LocalDate;
import java.util.List;

import com.QuesTyme.dto.SlotTiming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OneOnOne {

	private String title;
	private String instruction;
	private Integer adminId;
	private String meetingLink;
	private LocalDate date;
	private List<SlotTiming> slotTime;
	private Integer duration;
	private String type;
	private String AdminName;
	
	

}
