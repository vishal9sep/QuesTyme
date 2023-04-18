package com.QuesTyme.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDto {

	private String day;

	private boolean isChecked;

	private List<SlotTiming> slotTiming;

}
