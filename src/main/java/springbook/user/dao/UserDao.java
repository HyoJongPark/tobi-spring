package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<User> userMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        };
    }

    public void add(final User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, userMapper());
    }

    public void clearAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", userMapper());
    }

//    public void jdbcContextWithStatementStrategy(StatementStrategy smt) throws SQLException {
//        Connection c = null;
//        PreparedStatement ps = null;
//
//        try {
//            c = dataSource.getConnection();
//            ps = smt.makePreparedStatement(c);
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//                }
//            }
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//                }
//            }
//        }
//    }
}
