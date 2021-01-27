package org.shaneking.leon.rr.cfg;

import org.shaneking.leon.rr.interceptor.ReqIpsInterceptor;
import org.shaneking.leon.rr.interceptor.ReqUrlInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RrCfg implements WebMvcConfigurer {
  @Value("${sk.leon.rr.req.ips.enabled:true}")
  private boolean reqIpsEnabled;

  @Value("${sk.leon.rr.req.url.enabled:true}")
  private boolean reqUrlEnabled;

  @Autowired(required = false)
  private ReqIpsInterceptor reqIpsInterceptor;

  @Autowired(required = false)
  private ReqUrlInterceptor reqUrlInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    if (reqIpsEnabled) {
      registry.addInterceptor(reqIpsInterceptor).addPathPatterns("/**");
    }
    if (reqUrlEnabled) {
      registry.addInterceptor(reqUrlInterceptor).addPathPatterns("/**");
    }
  }
}
