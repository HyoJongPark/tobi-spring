package springbook.learningtest.jdk;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    void invokeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "Spring";

        //length()
        assertThat(name.length()).isEqualTo(6);

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name)).isEqualTo(6);

        //charAt()
        assertThat(name.charAt(0)).isEqualTo('S');

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character) charAtMethod.invoke(name, 0)).isEqualTo('S');
    }

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Tobi")).isEqualTo("HELLO TOBI");
        assertThat(proxiedHello.sayHi("Tobi")).isEqualTo("HI TOBI");
        assertThat(proxiedHello.sayThankYou("Tobi")).isEqualTo("THANK YOU TOBI");
    }

    @Test
    void dynamicProxy() {
        //(클래스로더, 구현할 인터페이스, 부가기능과 위임 코드를 담은 InvocationHandler)
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UpperCaseHandler(new HelloTarget())
        );
        assertThat(proxiedHello.sayHello("Tobi")).isEqualTo("HELLO TOBI");
        assertThat(proxiedHello.sayHi("Tobi")).isEqualTo("HI TOBI");
        assertThat(proxiedHello.sayThankYou("Tobi")).isEqualTo("THANK YOU TOBI");
    }
}
