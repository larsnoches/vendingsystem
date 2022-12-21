package org.cyrilselyanin.vendingsystem.regularbus.dto.bus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
		"id", "makeModel", "manufacturerCountry",
		"yearOfManufacture", "seatCount", "regNumber",
		"carrier"
})
public class GetBusResponseDto extends BasicBusRequestDto {

	private Long id;
//	private String makeModel;
//	private String manufacturerCountry;
//	private Integer yearOfManufacture;
//	private Integer seatCount;
//	private String regNumber;
//	private GetCarrierResponseDto carrier;



}
