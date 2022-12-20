package org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GetBusPointResponseDto extends BasicBusPointRequestDto {

	private Long id;

}
