package org.shaneking.leon.swagger.cfg;

import org.shaneking.leon.swagger.prop.WebSwaggerProps;
import org.shaneking.ling.zero.lang.String0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Optional;
import java.util.function.Predicate;

@Configuration
@EnableOpenApi
public class WebSwaggerCfg implements WebMvcConfigurer {
  @Autowired
  private WebSwaggerProps props;

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .enable(props.isEnabled())
      .useDefaultResponseMessages(false)
      .select()
      .apis(predicate(props.getBasePkg()))
//      .apis(RequestHandlerSelectors.any())
      .paths(PathSelectors.regex(props.getPathReg()))
      .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title(props.getTitle()).version(props.getVersion()).build();
  }

  private Predicate<RequestHandler> predicate(final String pkgs) {
    return requestHeader -> Optional.ofNullable(requestHeader.declaringClass()).map(clazz -> {
      for (String pkg : pkgs.split(String0.SEMICOLON)) {
        boolean matched = ClassUtils.getPackageName(clazz).startsWith(pkg);
        if (matched) {
          return true;
        }
      }
      return false;
    }).orElse(true);
  }
}
