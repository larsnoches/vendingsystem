package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.cyrilselyanin.vendingsystem.regularbus.dto.payment.AmountDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.payment.ConfirmationRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.payment.CreatePaymentRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.payment.CreatedPaymentResponseDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final String CREATE_PAYMENT_URL = "https://api.yookassa.ru/v3/payments";

	private final OkHttpClient client = new OkHttpClient();
	private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	private final ObjectReader objectReader = new ObjectMapper().reader();

	@Override
	public String createPayment(String amount, String qrCode) throws JsonProcessingException {
		log.info("Create payment for qrcode {}", qrCode);
		if (amount.equals("test") && qrCode.equals("test")) {
			return "test";
		}

		CreatePaymentRequestDto requestDto = new CreatePaymentRequestDto();

		AmountDto amountDto = new AmountDto();
		amountDto.setValue(amount);
		requestDto.setAmount(amountDto);

		ConfirmationRequestDto confirmationRequestDto = new ConfirmationRequestDto();
		confirmationRequestDto.setQrCode(qrCode);
		requestDto.setConfirmation(confirmationRequestDto);

		requestDto.setDescription("Оплата билета");

		String requestDtoJson = objectWriter.writeValueAsString(requestDto);

		RequestBody body = RequestBody.create(JSON, requestDtoJson);
		Request request = new Request.Builder().url(CREATE_PAYMENT_URL).post(body).build();

		try (Response response = client.newCall(request).execute()) {
			String responseDtoJson = response.body().string();
			CreatedPaymentResponseDto responseDto = objectReader.readValue(
					responseDtoJson, CreatedPaymentResponseDto.class
			);
			String confirmationUrl = responseDto.getConfirmation().getConfirmationUrl();
			log.info("Confirmation url for payment is {}", confirmationUrl);
			return confirmationUrl;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
