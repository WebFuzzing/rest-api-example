package org.example;

import org.evomaster.client.java.controller.EmbeddedSutController;
import org.evomaster.client.java.controller.InstrumentedSutStarter;
import org.evomaster.client.java.controller.api.dto.AuthenticationDto;
import org.evomaster.client.java.controller.api.dto.SutInfoDto;
import org.evomaster.client.java.controller.api.dto.database.schema.DatabaseType;
import org.evomaster.client.java.controller.db.DbCleaner;
import org.evomaster.client.java.controller.internal.SutController;
import org.evomaster.client.java.controller.internal.db.DbSpecification;
import org.evomaster.client.java.controller.problem.ProblemInfo;
import org.evomaster.client.java.controller.problem.RestProblem;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class EMDriver extends EmbeddedSutController {

    public static void main(String[] args){

        SutController controller = new EMDriver();
        InstrumentedSutStarter starter = new InstrumentedSutStarter(controller);

        starter.start();
    }

    private ConfigurableApplicationContext ctx;

    private Connection sqlConnection;

    public boolean isSutRunning() {
        return ctx!=null && ctx.isRunning();
    }

    public String getPackagePrefixesToCover() {
        return "org.example";
    }

    public List<AuthenticationDto> getInfoForAuthentication() {
        return null;
    }


    public ProblemInfo getProblemInfo() {
        return new RestProblem("http://localhost:8080/v3/api-docs", null);
    }

    public SutInfoDto.OutputFormat getPreferredOutputFormat() {
        return SutInfoDto.OutputFormat.JAVA_JUNIT_5;
    }

    public String startSut() {

        ctx = SpringApplication.run(Application.class);

        JdbcTemplate jdbc = ctx.getBean(JdbcTemplate.class);

        try {
            sqlConnection = jdbc.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return "http://localhost:8080";
    }

    public void stopSut() {
        ctx.stop();
    }

    public void resetStateOfSUT() {
    }

    @Override
    public List<DbSpecification> getDbSpecifications() {
        return Arrays.asList(new DbSpecification(DatabaseType.H2, sqlConnection));
    }
}
