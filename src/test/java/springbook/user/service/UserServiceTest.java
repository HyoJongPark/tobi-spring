package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
            new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
            new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
            new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    void upgradeLevels() throws SQLException {
        userDao.clearAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }

    @Test
    void add() {
        userDao.clearAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(userWithoutLevel.getLevel());
    }

    @Test
    void upgradeAllOrNothing() throws Exception{
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setDataSource(dataSource);

        userDao.clearAll();
        for (User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserService.TestUserServiceException e) {
        }
        checkLevel(users.get(1), false);
    }

    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }
}
