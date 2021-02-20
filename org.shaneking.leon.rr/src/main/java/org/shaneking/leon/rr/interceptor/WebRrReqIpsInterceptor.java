package org.shaneking.leon.rr.interceptor;

import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.zero.persistence.Tuple;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

///Filter -> Servlet -> Interceptor -> Controller
///why Interceptor not Filter? Because exception can be process by GlobalExceptionHandler
@Component
@ConditionalOnProperty(prefix = "sk.leon.rr.req.ips", value = "enabled", matchIfMissing = true)
public class WebRrReqIpsInterceptor implements HandlerInterceptor {
  public static final ThreadLocal<String> REQ_IPS = new ThreadLocal<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    REQ_IPS.set(OM3.writeValueAsString(Tuple.of(request.getHeader("X-Forwarded-For"), request.getHeader("X-Real-IP"), request.getRemoteAddr())));
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    REQ_IPS.remove();
  }
}
