package com.agritsik.patterns.cor;


import java.util.ArrayList;
import java.util.List;

public class Observer {

    public static void main(String[] args) {

        MyPublisher<String> myPublisher = new MyPublisher<>();
        myPublisher.register(new FirstSubscriber(), new SecondSubscriber());

        myPublisher.publish("message1");
        myPublisher.publish("message2");
        myPublisher.publish("message3");
    }
}

/**
 * Publishes the event to all registered subscribers
 *
 * @param <T> the type of the event
 */
class MyPublisher<T> {

    private List<Subscriber<T>> subscribers = new ArrayList<>();

    void register(Subscriber<T>... subscribers) {
        this.subscribers.addAll(List.of(subscribers));
    }

    void register(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    void publish(T event) {
        subscribers.forEach(t -> t.onNext(event));
    }

}

/**
 * Subscribes to events
 *
 * @param <T> the type of the event
 */
interface Subscriber<T> {
    void onNext(T event);
}

class FirstSubscriber implements Subscriber<String> {
    @Override
    public void onNext(String req) {
        System.out.println("FirstSubscriber: " + req);
    }
}

class SecondSubscriber implements Subscriber<String> {
    @Override
    public void onNext(String req) {
        System.out.println("SecondSubscriber: " + req);
    }
}

