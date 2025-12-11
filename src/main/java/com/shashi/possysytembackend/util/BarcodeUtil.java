package com.shashi.possysytembackend.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;

public class BarcodeUtil {

    public static BufferedImage generateBarcodeImage(String barcodeText) throws WriterException {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 400, 120);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
