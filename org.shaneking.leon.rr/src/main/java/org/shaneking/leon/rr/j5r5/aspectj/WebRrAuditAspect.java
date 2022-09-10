package org.shaneking.leon.rr.j5r5.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.shaneking.leon.rr.j5f5interceptor.WebRrReqIpsInterceptor;
import org.shaneking.leon.rr.j5f5interceptor.WebRrReqUrlInterceptor;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.persistence.entity.sql.RrAuditLogEntities;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.zero.annotation.ZeroAnnotation;
import org.shaneking.ling.zero.text.MF0;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.shaneking.roc.rr.aspectj.RrAuditAspect;
import org.shaneking.roc.rr.aspectj.RrTenantAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @see org.shaneking.roc.rr.aspectj.RrAuditAspect#ORDER
 * @see RrTenantAspect#ORDER
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "sk.leon.rr.audit", value = "enabled", matchIfMissing = true)
@Slf4j
@Order(WebRrAuditAspect.ORDER)
public class WebRrAuditAspect {
  public static final int ORDER = RrAuditAspect.ORDER + RrAuditAspect.ORDER / 1000;

  @Value("${sk.leon.rr.audit.enabled:true}")
  private boolean enabled;

  @Before("pointcut() && @annotation(rrAudit)")
  public void before(JoinPoint jp, RrAudit rrAudit) throws Throwable {
    if (enabled) {
      if (jp.getArgs().length > rrAudit.reqParamIdx() && jp.getArgs()[rrAudit.reqParamIdx()] instanceof Req) {
        Req<?> req = (Req<?>) jp.getArgs()[rrAudit.reqParamIdx()];

        RrAuditLogEntities auditLogEntity = req.gnnCtx().getAuditLog();
        if (auditLogEntity != null) {
          auditLogEntity.setReqIps(WebRrReqIpsInterceptor.REQ_IPS.get());
          auditLogEntity.setReqUrl(WebRrReqUrlInterceptor.REQ_URL.get());
        }
      } else {
        log.error(MF0.fmt("{0} - {1} : {2}", ZeroAnnotation.ERR_CODE__ANNOTATION_SETTING_ERROR, jp.getSignature().toLongString(), OM3.writeValueAsString(rrAudit)));
      }
    }
  }

  @Pointcut("execution(@org.shaneking.roc.rr.annotation.RrAudit * *..*.*(..))")
  private void pointcut() {
  }
}
