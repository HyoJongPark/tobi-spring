package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KConnectionMaker implements ConnectionMaker{

    //현재는 같은 로직이지만 인터페이스의 구현으로 변경에 용이함을 표현하기위해 남겨놓음
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/tobi_spring", "root", "qkr78097809*"
        );
        return c;
    }
}
