package com.jamycake.chronicler;

import com.jamycake.chronicler.publisher.FileModifiedPublisher;
import com.jamycake.chronicler.publisher.Publisher;
import com.jamycake.chronicler.subscriber.ReceiptRewriter;
import com.jamycake.chronicler.subscriber.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Chronicler  {

    private static final String NOT_ENOUGH_ARGUMENTS = "Not enough command line arguments";
    private static final String HELP = "Use [path to tracked file] [path to destination folder] [interval ms]";
    private static final String DESCRIPTION = "This Chronicler track the last modified time of the receipt file " +
            "\nthat you choose and copy it`s state to the chosen folder";
    private static final String CHRONICLER_STARTED = "Chronicler started. Close your terminal or interrupt " +
            "process if you want to stop it";

    private Publisher publisher;

    public Chronicler(Publisher publisher, Subscriber subscriber) {
        this.publisher = publisher;

        publisher.addSubscriber(subscriber);
    }

    public static void main(String [] args) throws Exception{
        if (args.length > 0){
            Chronicler chronicler = Chronicler.getInstance(args);
            printMessage(CHRONICLER_STARTED);
            chronicler.start();
        } else {
            printMessage(NOT_ENOUGH_ARGUMENTS + "\n" + DESCRIPTION + "\n" + HELP);
        }

    }

    public static void printMessage(String message){
        System.out.println(message);
    }

    private static Chronicler getInstance(String[] args) throws FileNotFoundException{
        File modifiedFile = new File(args[0]);
        File destinationFolder = new File(args[1]);

        if (!modifiedFile.exists()) throw new FileNotFoundException(modifiedFile.getAbsolutePath());
        if (!destinationFolder.exists()) throw new FileNotFoundException(modifiedFile.getAbsolutePath());

        final long WORKING_INTERVAL = Long.parseLong(args[2]);

        Subscriber subscriber = new ReceiptRewriter(modifiedFile, destinationFolder);
        Publisher publisher = new FileModifiedPublisher(modifiedFile, new ArrayList<>(), WORKING_INTERVAL);
        return new Chronicler(publisher, subscriber);
    }

    public void start() throws Exception{
        publisher.on();
    }
}
