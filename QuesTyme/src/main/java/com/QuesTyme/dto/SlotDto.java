package com.QuesTyme.dto;

import java.time.LocalTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.QuesTyme.entity.OneOnOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotDto {

	private Integer slotId;

	private OneOnOne oneOnOne;

	private LocalTime startTime;
	private LocalTime endTime;
	private char status;
	private String name;
	private String userEmail;
	private String topic;
}
