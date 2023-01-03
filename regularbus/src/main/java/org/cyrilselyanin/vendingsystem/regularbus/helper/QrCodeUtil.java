package org.cyrilselyanin.vendingsystem.regularbus.helper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class QrCodeUtil {

	private QrCodeUtil() {
		//
	}

	public static BufferedImage generateQrCodeImage(String barcodeText) throws Exception {
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(
				barcodeText, BarcodeFormat.QR_CODE, 200, 200
		);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

}
