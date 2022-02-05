package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class CountingDaoFactoryTest {

    public static void main(String[] args) {
        //자바코드로 빈 등록시에 사용
//        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        //xml 사용시에 사용
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("Connection counter : " + ccm.getCounter());
    }

}