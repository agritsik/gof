package com.agritsik.patterns.cor;


//import com.sun.xml.internal.messaging.saaj.util.Base64;

import com.google.common.base.Charsets;

import java.util.Base64;
import java.util.Set;

/*

 Decorator is a structural design pattern that lets you attach new behaviors to objects by placing these objects
 inside special wrapper objects that contain the behaviors.

 Solution:
  - Wrapper is the alternative nickname for the Decorator pattern that clearly expresses the main idea of the pattern.
    A “wrapper” is an object that can be linked with some “target” object. The wrapper contains the same set of methods
    as the target and delegates to it all requests it receives. However, the wrapper may alter the result by doing
    something either before or after it passes the request to the target.

 When:
  - Use the Decorator pattern when you need to be able to assign extra behaviors to objects at runtime without
    breaking the code that uses these objects.

 Links:
  - https://en.wikipedia.org/wiki/Decorator_pattern
  - https://refactoring.guru/design-patterns/decorator

 */
public class Decorator {

    public static void main(String[] args) {
        Set<String> arguments = Set.of(args);

        Reader source = new FileReader();

        if (arguments.contains("--decode")) {
            source = new EncoderReaderDecorator(source);
        }

        if (arguments.contains("--uppercase")) {
            source = new UppercaseReaderDecorator(source);
        }

        System.out.println(source.read());
    }
}

/**
 * The Component Interface declares the common interface for both wrappers and wrapped objects.
 */
interface Reader {
    String read();
}

/**
 * The Concrete Component is a class of objects being wrapped.
 * It defines the basic behavior, which can be altered by decorators.
 */
class FileReader implements Reader {
    public String read() {
        return "aGVsbG8gd29ybGQh";
    }
}

/**
 * Abstract Decorator delegates all operations to the wrapped object.
 */
abstract class ReaderDecorator implements Reader {
    private Reader target;

    public ReaderDecorator(Reader target) {
        this.target = target;
    }

    public String read() {
        return target.read();
    }
}

/**
 * Concrete Decorator overrides methods of the base decorator and
 * execute their behavior either before or after calling the parent method.
 */
class EncoderReaderDecorator extends ReaderDecorator {
    public EncoderReaderDecorator(Reader target) {
        super(target);
    }

    @Override
    public String read() {
        byte[] bytes = super.read().getBytes(Charsets.UTF_8);
        return new String(Base64.getDecoder().decode(bytes));
    }
}

class UppercaseReaderDecorator extends ReaderDecorator {
    public UppercaseReaderDecorator(Reader target) {
        super(target);
    }

    @Override
    public String read() {
        return super.read().toUpperCase();
    }
}

