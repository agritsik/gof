package com.agritsik.patterns.cor;

/*

 Chain of Responsibility is a behavioral design pattern that lets you pass requests along a chain of handlers.
 Upon receiving a request, each next decides either to process the request or to pass it to the next next
 in the chain.

 Details:
  - The Chain of Responsibility relies on transforming particular behaviors into stand-alone objects called handlers.
    The pattern suggests that you link these handlers into a chain. The request travels along the chain until all
    handlers have had a chance to process it. A next can decide not to pass the request further down the chain
  - Use the Chain of Responsibility pattern when your program is expected to process different kinds of requests
    in various ways, but the exact types of requests and their sequences are unknown beforehand.

 Links:
  - https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern
  - https://refactoring.guru/design-patterns/chain-of-responsibility

 */
public class ChainOfResponsibility {
    public static void main(String[] args) {

        Handler handler = new ConcreteHandlerA();
        handler.setNext(new ConcreteHandlerB()).setNext(new ConcreteHandlerC());

        handler.handle("A");
        handler.handle("B");
        handler.handle("C");
    }
}

/**
 * The Handler defines a field for storing a reference to the next handler.
 * The clients can build a chain by passing a handler to the constructor
 * or setter of the previous handler.
 */
abstract class Handler {
    private Handler next;

    Handler setNext(Handler handler) {
        this.next = handler;
        return next;
    }

    boolean checkNext(String q) {
        if (next == null) return true;
        return next.handle(q);
    }

    abstract boolean handle(String q);
}

class ConcreteHandlerA extends Handler {
    boolean handle(String q) {
        if ("A".equals(q)) {
            System.out.println("processed by ConcreteHandlerA");
            return true;
        }
        return checkNext(q);
    }
}

class ConcreteHandlerB extends Handler {
    boolean handle(String q) {
        if ("B".equals(q)) {
            System.out.println("processed by ConcreteHandlerB");
            return true;
        }
        return checkNext(q);
    }
}

class ConcreteHandlerC extends Handler {
    boolean handle(String q) {
        System.out.println("processed by ConcreteHandlerC");
        return true;
    }
}


