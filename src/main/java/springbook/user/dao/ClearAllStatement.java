package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//전략 클래스를 통해 구현된 ClearAll 메소드
public class ClearAllStatement implements StatementStrategy{
    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        return c.prepareStatement("delete from users");
    }
}
