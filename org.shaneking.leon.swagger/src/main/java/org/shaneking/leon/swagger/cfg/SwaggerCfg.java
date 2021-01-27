package org.shaneking.leon.swagger.cfg;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.shaneking.leon.swagger.prop.SwaggerProps;
import org.shaneking.ling.zero.lang.String0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerCfg implements WebMvcConfigurer {
  @Autowired
  private SwaggerProps swaggerProps;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (swaggerProps.isEnabled()) {
      registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
      registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
  }

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .enable(swaggerProps.isEnabled())
      .useDefaultResponseMessages(false)
      .select()
      .apis(predicate(swaggerProps.getBasePkg()))
      .paths(PathSelectors.regex(swaggerProps.getPathReg()))
      .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title(swaggerProps.getTitle()).version(swaggerProps.getVersion()).build();
  }

  private Predicate<RequestHandler> predicate(final String pkgs) {
    return requestHeader -> Optional.fromNullable(requestHeader.declaringClass()).transform(clazz -> {
      for (String pkg : pkgs.split(String0.SEMICOLON)) {
        boolean matched = ClassUtils.getPackageName(clazz).startsWith(pkg);
        if (matched) {
          return true;
        }
      }
      return false;
    }).or(true);
  }
}
