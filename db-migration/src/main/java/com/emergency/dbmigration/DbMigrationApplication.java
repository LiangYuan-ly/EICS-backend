package com.emergency.dbmigration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DbMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbMigrationApplication.class, args);
    }
    // 关键：通过 CommandLineRunner 触发 Flyway
    @Bean
    public CommandLineRunner migrate(@Autowired Flyway flyway) {
        return args -> {
            System.out.println("开始执行 Flyway 数据库迁移...");
            flyway.migrate();
            System.out.println("Flyway 迁移完成！");
        };
    }
}
