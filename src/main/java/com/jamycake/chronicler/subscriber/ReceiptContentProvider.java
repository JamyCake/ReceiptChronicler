package com.jamycake.chronicler.subscriber;

import java.io.*;

public class ReceiptContentProvider {

    private static final int BUFFER_SIZE = 1024;
    private static final char END_OF_FILE = '~';
    private static final String EMPTY_STRING = "";
    private static final long ATTEMPT_READING_INTERVAL = 1000;
    private static final String KOI8_R_CS = "KOI8-R";

    private final File receipt;



    public ReceiptContentProvider(final String path) throws FileNotFoundException {
        this.receipt = new File(path);
        if (!receipt.exists()) throw new FileNotFoundException(receipt.getAbsolutePath());
    }

    public String getReceiptContent(){
        try {
            for (;;){
                if (receipt.canRead()){
                    return getResult();
                }
                Thread.sleep(ATTEMPT_READING_INTERVAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResult() throws Exception {
        byte [] rawBytes = readRawBytesFromFile(receipt);
        String result = new String(rawBytes, KOI8_R_CS);
        return result.replaceAll("\\x00{4,}", EMPTY_STRING);
    }

    private byte[] readRawBytesFromFile(final File file) throws IOException {
        final byte[] buffer = new byte[BUFFER_SIZE];
        final FileInputStream inputStream = new FileInputStream(file);

        for (int i = 0; i < BUFFER_SIZE; ++i) {
            final byte byteValue = (byte) inputStream.read();
            if (byteValue == END_OF_FILE) {
                break;
            }
            buffer[i] = byteValue;
        }

        inputStream.close();
        return buffer;
    }

}