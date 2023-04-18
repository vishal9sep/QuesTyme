package com.QuesTyme.entity;


import java.time.LocalTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Availability {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int availabilityId = 1;

	@Column(name = "avail_day")
	private String day;
	
	@JsonFormat(pattern = "HH:mm")
	@Column(name = "start_time")
	private LocalTime startTime;

	@Column(name = "end_time")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;
	
//	@ElementCollection
//	private List<SlotTiming> slotTiming;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "recurringMeeting_id")
	private RecurringMeeting recurringMeeting;

}
