package org.shaneking.leon.demo4web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Demo4WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(Demo4WebApplication.class, args);
  }
}
