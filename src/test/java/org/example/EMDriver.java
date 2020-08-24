package org.example;

import com.p6spy.engine.spy.P6SpyDriver;
import org.evomaster.client.java.controller.EmbeddedSutController;
import org.evomaster.client.java.controller.InstrumentedSutStarter;
import org.evomaster.client.java.controller.api.dto.AuthenticationDto;
import org.evomaster.client.java.controller.api.dto.SutInfoDto;
import org.evomaster.client.java.controller.db.DbCleaner;
import org.evomaster.client.java.controller.internal.SutController;
import org.evomaster.client.java.controller.problem.ProblemInfo;
import org.evomaster.client.java.controller.problem.RestProblem;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EMDriver extends EmbeddedSutController {

    public static void main(String[] args){

        SutController controller = new EMDriver();
        InstrumentedSutStarter starter = new InstrumentedSutStarter(controller);

        starter.start();
    }

    private ConfigurableApplicationContext ctx;
    private Connection connection;

    public boolean isSutRunning() {
        return ctx!=null && ctx.isRunning();
    }

    public String getPackagePrefixesToCover() {
        return "org.example";
    }

    public List<AuthenticationDto> getInfoForAuthentication() {
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDatabaseDriverName() {
        return "org.h2.Driver";
    }

    public ProblemInfo getProblemInfo() {
        return new RestProblem("http://localhost:8080/v3/api-docs", null);
    }

    public SutInfoDto.OutputFormat getPreferredOutputFormat() {
        return SutInfoDto.OutputFormat.JAVA_JUNIT_5;
    }

    public String startSut() {

        ctx = SpringApplication.run(Application.class, new String[]{
                "--spring.datasource.url=jdbc:p6spy:h2:mem:testdb;DB_CLOSE_DELAY=-1;",
                "--spring.datasource.driver-class-name=" + P6SpyDriver.class.getName()
        });

        JdbcTemplate jdbc = ctx.getBean(JdbcTemplate.class);
        try {
            connection = jdbc.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return "http://localhost:8080";
    }

    public void stopSut() {
        ctx.stop();
    }

    public void resetStateOfSUT() {
        DbCleaner.clearDatabase_H2(connection);
    }
}
