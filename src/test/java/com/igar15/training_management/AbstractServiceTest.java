package com.igar15.training_management;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@SpringBootTest
@Sql(scripts = "classpath:db_scripts/populate_db.sql", config = @SqlConfig(encoding = "UTF-8"))
public class AbstractServiceTest {

}
