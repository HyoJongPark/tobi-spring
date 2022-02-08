package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

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

        dao = new UserDao();

        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/tobi_spring", "tobi_spring", "test", true);
        dao.setDataSource(dataSource);
    }

    //main 메소드 -> JUnit 사용해 변경
    @Test
    public void addAndGet() {
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
    public void getCount() {
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
    public void getUserFailure() {
        dao.clearAll();
        assertThrows(EmptyResultDataAccessException.class,() -> dao.get("error_id"));
    }

    @Test
    void getAll() {
        dao.clearAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users1.size()).isEqualTo(3);
        checkSameUser(user1, users1.get(0));
        checkSameUser(user2, users1.get(1));
        checkSameUser(user3, users1.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}