package com.jamycake.chronicler.subscriber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiptRewriter implements Subscriber {

    private final static String RECEIPT_REWROTE = "Receipt \"%s\" rewrote to \"%s\"\n";

    private final File rewroteReceipt;
    private String receiptContent;
    private final File destinationFolder;

    public ReceiptRewriter(final File rewroteReceipt,
                           final File destinationFolder)
    {
        this.rewroteReceipt = rewroteReceipt;
        this.destinationFolder = destinationFolder;
    }



    @Override
    public void update() throws IOException{
        receiptContent = getReceiptContent();
        rewrite();
    }



    private String getReceiptContent() throws IOException {
        ReceiptContentProvider contentProvider = new ReceiptContentProvider(rewroteReceipt.getAbsolutePath());
        String KOI8_R_CS = "KOI8-R";
        contentProvider.setCharset(KOI8_R_CS);
        return contentProvider.getReceiptContent();
    }



    private void rewrite() throws IOException {
        File fileForWriting = getFileForWriting();
        writeToFile(fileForWriting);

        printMessage(fileForWriting.getName(), destinationFolder.getAbsolutePath());
    }

    private File getFileForWriting() throws IOException{
        String wroteFileName = getName();
        File file = new File(destinationFolder, wroteFileName);
        file.createNewFile();
        return file;
    }

    private String getName() {
        String [] strings = receiptContent.split("[\n\r]+");
        SberbankReceiptNameProvider nameProvider = new SberbankReceiptNameProvider();
        return nameProvider.getName(strings);
    }



    private void writeToFile(File fileForWriting) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(fileForWriting);

        final byte EOF = 0;
        byte [] bytes = receiptContent.getBytes();
        for (byte b : bytes){
            if (b == EOF) break;
            outputStream.write(b);
        }

        outputStream.close();
    }

    private void printMessage(String ... mes) {
        System.out.printf(RECEIPT_REWROTE, mes[0], mes[1]);
    }

}
