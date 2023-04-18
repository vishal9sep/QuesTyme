package com.QuesTyme.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer slotId;

	private String title;
	private String instruction;
	private Integer adminId;
	private String meetingLink;

	@Column(name="slot_date")
	private LocalDate date;
	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;
	private String day;
	private String type;
	private char status;
	private Integer userId;
	private Integer recurringId;
	
}
