package org.shaneking.leon.rr.g5e5interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@ConditionalOnProperty(prefix = "sk.leon.rr.req.url", value = "enabled", matchIfMissing = true)
public class WebRrReqUrlInterceptor implements HandlerInterceptor {
  public static final ThreadLocal<String> REQ_URL = new ThreadLocal<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    REQ_URL.set(request.getRequestURI());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    REQ_URL.remove();
  }
}
