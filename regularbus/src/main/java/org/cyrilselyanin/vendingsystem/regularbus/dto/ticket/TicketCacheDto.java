package org.cyrilselyanin.vendingsystem.regularbus.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;

@AllArgsConstructor
@Getter
public class TicketCacheDto {

	Ticket ticket;
	GetBusTripResponseDto busTripDto;

}
