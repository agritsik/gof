package com.agritsik.patterns.cor;


import java.util.ArrayList;
import java.util.List;

public class Observer {

    public static void main(String[] args) throws InterruptedException {

        MyPublisher<String> myPublisher = new MyPublisher<>();
        myPublisher.register(new FirstTracker());
        myPublisher.register(new SecondTracker());

        myPublisher.publish("message1");
        myPublisher.publish("message2");
    }
}


class MyPublisher<T> {

    private List<RankingTracker<T>> trackers;

    public MyPublisher() {
        this.trackers = new ArrayList<>();
    }

    void register(RankingTracker<T> tracker) {
        trackers.add(tracker);
    }

    void publish(T event) {
        trackers.forEach(t -> t.track(event));
    }

}

interface RankingTracker<T> {
    void track(T event);
}


class SecondTracker implements RankingTracker<String> {

    public void track(String req) {
        System.out.println("SecondTracker: " + req);
    }


}

class FirstTracker implements RankingTracker<String> {

    public void track(String req) {
        System.out.println("FirstTracker: " + req);
    }

}

