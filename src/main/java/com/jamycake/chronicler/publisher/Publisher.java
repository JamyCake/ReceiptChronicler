package com.jamycake.chronicler.publisher;

import com.jamycake.chronicler.subscriber.Subscriber;

public interface Publisher {
    void on() throws Exception;
    void addSubscriber(Subscriber subscriber);
    void notifySubscribers()throws Exception ;
}
