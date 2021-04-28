package sktest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties
@EnableAspectJAutoProxy
@SpringBootApplication
public class SpringMvcUnitApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringMvcUnitApplication.class, args);
  }
}
