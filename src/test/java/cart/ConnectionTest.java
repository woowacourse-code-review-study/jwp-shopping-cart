package cart;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("데이터베이스 커넥션 연결을 확인한다.")
    void getConnection() throws SQLException {
        //given
        final Connection connection = dataSource.getConnection();

        //when
        //then
        assertThat(connection).isNotNull();
    }
}
