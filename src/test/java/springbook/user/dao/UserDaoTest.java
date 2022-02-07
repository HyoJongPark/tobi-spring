package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = "/test-applicationContext.xml")
//@DirtiesContext
class UserDaoTest {

//    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        System.out.println("this = " + this);

        this.user1 = new User("soodo", "박효종", "hi");
        this.user2 = new User("sorisori", "수리수리", "bi");
        this.user3 = new User("masori", "마수리", "tk");

//        DataSource dataSource = new SingleConnectionDataSource(
//                "jdbc:mysql://localhost/testdb", "tobi_spring", "test", true);
//        dao.setDataSource(dataSource);

        dao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/tobi_spring", "tobi_spring", "test", true);
        dao.setDataSource(dataSource);
    }

    //main 메소드 -> JUnit 사용해 변경
    @Test
    public void addAndGet() throws SQLException {
        dao.clearAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userGet1 = dao.get(user1.getId());
        assertThat(user1.getName()).isEqualTo(userGet1.getName());
        assertThat(user1.getPassword()).isEqualTo(userGet1.getPassword());

        User userGet2 = dao.get(user2.getId());
        assertThat(user2.getName()).isEqualTo(userGet2.getName());
        assertThat(user2.getPassword()).isEqualTo(userGet2.getPassword());

        dao.clearAll();
        assertThat(dao.getCount()).isEqualTo(0);

    }

    @Test
    public void getCount() throws SQLException {
        dao.clearAll();
        assertThat(dao.getCount()).isEqualTo(0);
        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);
        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);

        dao.clearAll();
        assertThat(dao.getCount()).isEqualTo(0);
    }

    @Test
    public void getUserFailure() throws SQLException {
        dao.clearAll();
        assertThrows(EmptyResultDataAccessException.class,() -> dao.get("error_id"));
    }
}