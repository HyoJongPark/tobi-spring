package springbook.learningtest.jdk.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.learningtest.jdk.Hello;
import springbook.learningtest.jdk.HelloTarget;
import springbook.learningtest.jdk.UpperCaseHandler;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicProxyTest {

    @Test
    void simpleProxy() {
        springbook.learningtest.jdk.Hello proxiedHello = (springbook.learningtest.jdk.Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{springbook.learningtest.jdk.Hello.class},
                new UpperCaseHandler(new springbook.learningtest.jdk.HelloTarget())
        );

        assertThat(proxiedHello.sayHello("Tobi")).isEqualTo("HELLO TOBI");
        assertThat(proxiedHello.sayHi("Tobi")).isEqualTo("HI TOBI");
        assertThat(proxiedHello.sayThankYou("Tobi")).isEqualTo("THANK YOU TOBI");
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Tobi")).isEqualTo("HELLO TOBI");
        assertThat(proxiedHello.sayHi("Tobi")).isEqualTo("HI TOBI");
        assertThat(proxiedHello.sayThankYou("Tobi")).isEqualTo("THANK YOU TOBI");
    }

    @Test
    void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Tobi")).isEqualTo("HELLO TOBI");
        assertThat(proxiedHello.sayHi("Tobi")).isEqualTo("HI TOBI");
        assertThat(proxiedHello.sayThankYou("Tobi")).isEqualTo("Thank You Tobi");
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }


    static interface Hello{
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello {
        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank You " + name;
        }
    }

}
