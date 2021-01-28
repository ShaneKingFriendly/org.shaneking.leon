package org.shaneking.leon.rr.cfg;

import org.shaneking.leon.rr.interceptor.WebRrReqIpsInterceptor;
import org.shaneking.leon.rr.interceptor.WebRrReqUrlInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebRrCfg implements WebMvcConfigurer {
  @Value("${sk.leon.rr.req.ips.enabled:true}")
  private boolean reqIpsEnabled;

  @Value("${sk.leon.rr.req.url.enabled:true}")
  private boolean reqUrlEnabled;

  @Autowired(required = false)
  private WebRrReqIpsInterceptor reqIpsInterceptor;

  @Autowired(required = false)
  private WebRrReqUrlInterceptor reqUrlInterceptor;

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
