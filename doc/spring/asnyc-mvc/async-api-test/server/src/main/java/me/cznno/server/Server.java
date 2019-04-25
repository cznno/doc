package me.cznno.server;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author cznno
 * Date: 2019/2/15
 */
@RestController
public class Server {
    RowMapper<String> rowMapper = (rs, rowNum) -> rs.getString("name");

    JdbcTemplate jdbcTemplate;

    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:h2:~/test");
        dataSource.setUsername("sa");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @GetMapping("foo")
    public Callable<List<String>> foo() {
        return () -> jdbcTemplate.query("select * from account", rowMapper);
    }

    @GetMapping("foo/timeout")
    public List<String> timeout() throws InterruptedException {
        Thread.sleep(1000 * 30);
        return jdbcTemplate.query("select * from account", rowMapper);
    }
}
