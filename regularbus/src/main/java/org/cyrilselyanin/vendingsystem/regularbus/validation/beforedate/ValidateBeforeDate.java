package org.cyrilselyanin.vendingsystem.regularbus.validation.beforedate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

//@Target( { ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Target( { ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = { })
@Constraint(validatedBy = { TicketBeforeDateValidator.class, BusTripBeforeDateValidator.class })
@Documented
public @interface ValidateBeforeDate {
    String message() default "The Start date should be before the ending date.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
