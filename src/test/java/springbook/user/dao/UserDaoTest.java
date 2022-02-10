package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Autowired
    DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("a", "박효종", "hi");
        this.user2 = new User("b", "수리수리", "bi");
        this.user3 = new User("c", "마수리", "tk");
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

        //getAll 메소드 사용시 id 알파벳 순서로 정렬되어 원하는 결과가 다른것에 영향을 받음
        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    @Test
    public void duplicateKey() {
        dao.clearAll();

        dao.add(user1);
        assertThrows(DataAccessException.class, () -> dao.add(user1));
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.clearAll();

        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            assertThat(set.translate(null, null, sqlEx)).isInstanceOf(DuplicateKeyException.class);
        }
    }

    private void checkSameUser(User userA, User userB) {
        assertThat(userA.getId()).isEqualTo(userB.getId());
        assertThat(userA.getName()).isEqualTo(userB.getName());
        assertThat(userA.getPassword()).isEqualTo(userB.getPassword());
    }
}