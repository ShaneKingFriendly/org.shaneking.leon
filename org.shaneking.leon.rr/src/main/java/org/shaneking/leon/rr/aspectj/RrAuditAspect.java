package org.shaneking.leon.rr.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.shaneking.leon.rr.interceptor.ReqIpsInterceptor;
import org.shaneking.leon.rr.interceptor.ReqUrlInterceptor;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.zero.annotation.ZeroAnnotation;
import org.shaneking.roc.persistence.entity.AuditLogEntity;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
@Slf4j
@Order(450)///must greater than org.shaneking.roc.rr.aspectj.RrAuditAspect
public class RrAuditAspect {
  @Value("${sk.roc.rr.audit.enabled:true}")
  private boolean enabled;

  @Pointcut("execution(@org.shaneking.roc.rr.annotation.RrAudit * *..*.*(..))")
  private void pointcut() {
  }

  @Before("pointcut() && @annotation(rrAudit)")
  public void before(JoinPoint jp, RrAudit rrAudit) throws Throwable {
    if (enabled) {
      if (jp.getArgs().length > rrAudit.reqParamIdx() && jp.getArgs()[rrAudit.reqParamIdx()] instanceof Req) {
        Req<?, ?> req = (Req<?, ?>) jp.getArgs()[rrAudit.reqParamIdx()];

        AuditLogEntity auditLogEntity = req.getCtx().getAuditLog();
        if (auditLogEntity != null) {
          auditLogEntity.setReqIps(ReqIpsInterceptor.REQ_IPS.get());
          auditLogEntity.setReqUrl(ReqUrlInterceptor.REQ_URL.get());
        }
      } else {
        log.error(MessageFormat.format("{0} - {1} : {2}", ZeroAnnotation.ERR_CODE__ANNOTATION_SETTING_ERROR, jp.getSignature().getName(), OM3.writeValueAsString(rrAudit)));
      }
    }
  }
}
