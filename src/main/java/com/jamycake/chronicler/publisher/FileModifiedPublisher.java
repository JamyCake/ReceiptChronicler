package com.jamycake.chronicler.publisher;

import com.jamycake.chronicler.subscriber.Subscriber;

import java.io.File;
import java.util.List;

public class FileModifiedPublisher implements Publisher {

    private boolean shouldWork;
    private long interval;

    private File observedFile;
    private long lastChangeTime = 0;

    private List<Subscriber> subscribers;

    public FileModifiedPublisher(final File file,
                                 final List<Subscriber> subscribers,
                                 final long interval)
    {
        observedFile = file;
        this.subscribers = subscribers;
        this.interval = interval;
    }


    @Override
    public void on() throws Exception{
        shouldWork = true;
        runWorkingCycle();
    }

    private void runWorkingCycle() throws Exception{
        while (shouldWork){            
            try {
                notifySubscribers();
                Thread.sleep(interval);
            } catch (InterruptedException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void notifySubscribers() throws Exception{
        long lastModified =  observedFile.lastModified();

        if (lastChangeTime < lastModified) {
            lastChangeTime = lastModified;
            for (Subscriber subscriber : subscribers){
                subscriber.update();
            }
        }
    }
}
