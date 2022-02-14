package springbook.user.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test
    void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) continue;
            user.setLevel(level);
            assertThrows(IllegalStateException.class,
                    () -> user.upgradeLevel());
        }
    }

}
