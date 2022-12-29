package org.cyrilselyanin.vendingsystem.regularbus.validation.beforedate;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BusTripBeforeDateValidator implements ConstraintValidator<ValidateBeforeDate, BusTrip> {

	@Override
	public void initialize(ValidateBeforeDate constraintAnnotation) {
	}

	@Override
	public boolean isValid(BusTrip busTrip, ConstraintValidatorContext context) {
		if (busTrip == null) {
			return true;
		}

		return busTrip.getDepartureDateTime().compareTo(
				busTrip.getArrivalDateTime()
		) < 0;
	}

}
