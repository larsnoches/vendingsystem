package org.cyrilselyanin.vendingsystem.regularbus.dto.seat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;

@NoArgsConstructor
@Getter
@Setter
public class GetSeatResponseDto {

	private Long id;
	private String name;
	private Boolean seatIsOccupied;
	private GetBusTripResponseDto busTrip;

}
