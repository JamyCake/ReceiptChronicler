package com.jamycake.chronicler.subscriber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptRewriter implements Subscriber {

    private final static String RECEIPT_REWROTE = "Receipt \"%s\" rewrote to \"%s\"\n";

    private final File rewroteReceipt;
    private String receiptContent;

    private final File destinationFolder;
    private final File currentDayFolder;

    public ReceiptRewriter(final File rewroteReceipt,
                           final File destinationFolder)
    {

        this.destinationFolder = destinationFolder;
        currentDayFolder = createCurrentDayFolder();
        this.rewroteReceipt = rewroteReceipt;
    }


    private File createCurrentDayFolder() {
        Date date = new Date();
        final String DATE_FORMAT = "dd.MM.yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String folderName = dateFormat.format(date);

        File folder = new File(destinationFolder, folderName);
        folder.mkdir();

        return folder;
    }


    @Override
    public void update() throws IOException{
        receiptContent = getReceiptContent();
        rewrite();
    }

    private String getReceiptContent() throws IOException {
        ReceiptContentProvider contentProvider = new ReceiptContentProvider(rewroteReceipt.getAbsolutePath());
        return contentProvider.getReceiptContent();
    }



    private void rewrite() throws IOException {
        File fileForWriting = getFileForWriting();
        writeToFile(fileForWriting);

        printMessage(fileForWriting.getName(), currentDayFolder.getAbsolutePath());
    }

    private File getFileForWriting() throws IOException{
        String wroteFileName = getName();
        File file = new File(currentDayFolder, wroteFileName);
        file.createNewFile();
        return file;
    }

    private String getName() {
        SberbankReceiptNameProvider nameProvider = new SberbankReceiptNameProvider();
        return nameProvider.getName(receiptContent);
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
