package com.agritsik.patterns.cor;


import com.sun.xml.internal.messaging.saaj.util.Base64;

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
        DataSourceDecorator dataSource = new EncoderDataSourceDecorator(new FileDataSource());
        System.out.println(dataSource.read());
    }
}

/**
 * The Component Interface declares the common interface for both wrappers and wrapped objects.
 */
interface DataSource {
    String read();
}

/**
 * The Concrete Component is a class of objects being wrapped.
 * It defines the basic behavior, which can be altered by decorators.
 */
class FileDataSource implements DataSource {
    public String read() {
        return "text from file";
    }
}

/**
 * Abstract Decorator delegates all operations to the wrapped object.
 */
abstract class DataSourceDecorator implements DataSource {
    private DataSource wrappee;

    public DataSourceDecorator(DataSource wrappee) {
        this.wrappee = wrappee;
    }

    public String read() {
        return wrappee.read(); // delegate to wrappee
    }
}

/**
 * Concrete Decorator overrides methods of the base decorator and
 * execute their behavior either before or after calling the parent method.
 */
class EncoderDataSourceDecorator extends DataSourceDecorator {

    public EncoderDataSourceDecorator(DataSource wrapee) {
        super(wrapee);
    }

    @Override
    public String read() {
        return Base64.base64Decode(super.read());
    }
}

