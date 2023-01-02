package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentService {

	String createPayment(String amount, String qrCode) throws JsonProcessingException;

}
