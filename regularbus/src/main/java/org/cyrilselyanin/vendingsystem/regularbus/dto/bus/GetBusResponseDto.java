package org.cyrilselyanin.vendingsystem.regularbus.dto.bus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;

@NoArgsConstructor
@Getter
@Setter
public class GetBusResponseDto {

	private Long id;
	private String makeModel;
	private String manufacturerCountry;
	private Integer yearOfManufacture;
	private Integer seatCount;
	private String regNumber;
	private GetCarrierResponseDto carrier;

}
