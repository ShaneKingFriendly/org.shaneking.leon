package org.shaneking.leon.rr.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.shaneking.leon.rr.interceptor.WebRrReqIpsInterceptor;
import org.shaneking.leon.rr.interceptor.WebRrReqUrlInterceptor;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.zero.annotation.ZeroAnnotation;
import org.shaneking.roc.persistence.entity.sql.AuditLogEntities;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
@ConditionalOnProperty(prefix = "sk.leon.rr.audit", value = "enabled")
@Slf4j
@Order(450)///400(org.shaneking.roc.rr.aspectj.RrAuditAspect) < 450 < 500(org.shaneking.roc.rr.aspectj.RrAccessAspect)
public class WebRrAuditAspect {
  @Value("${sk.leon.rr.audit.enabled:false}")
  private boolean enabled;

  @Pointcut("execution(@org.shaneking.roc.rr.annotation.RrAudit * *..*.*(..))")
  private void pointcut() {
  }

  @Before("pointcut() && @annotation(rrAudit)")
  public void before(JoinPoint jp, RrAudit rrAudit) throws Throwable {
    if (enabled) {
      if (jp.getArgs().length > rrAudit.reqParamIdx() && jp.getArgs()[rrAudit.reqParamIdx()] instanceof Req) {
        Req<?, ?> req = (Req<?, ?>) jp.getArgs()[rrAudit.reqParamIdx()];

        AuditLogEntities auditLogEntity = req.gnnCtx().getAuditLog();
        if (auditLogEntity != null) {
          auditLogEntity.setReqIps(WebRrReqIpsInterceptor.REQ_IPS.get());
          auditLogEntity.setReqUrl(WebRrReqUrlInterceptor.REQ_URL.get());
        }
      } else {
        log.error(MessageFormat.format("{0} - {1} : {2}", ZeroAnnotation.ERR_CODE__ANNOTATION_SETTING_ERROR, jp.getSignature().toLongString(), OM3.writeValueAsString(rrAudit)));
      }
    }
  }
}
