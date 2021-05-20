package org.shaneking.leon.rr.j5j5advice;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.rr.RespException;
import org.shaneking.ling.zero.lang.String0;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//https://www.jianshu.com/p/53ec69f1236e
@ConditionalOnProperty(prefix = "sk.leon.rr.controller.advice", value = "enabled", matchIfMissing = true)
@ControllerAdvice
@Slf4j
public class WebRrControllerAdvice {

  @ExceptionHandler(value = Throwable.class)
  @ResponseBody
  public String error(Throwable t) {
    log.error(t.toString(), t);
    Resp resp = Resp.failed(Resp.CODE_UNKNOWN_EXCEPTION, String0.null2EmptyTo(t.getMessage(), t.toString()));
    if (t instanceof RespException && ((RespException) t).getResp() != null) {
      Resp tmp = ((RespException) t).getResp();
      resp.setCode(String0.null2EmptyTo(tmp.getCode(), resp.getCode()));
      resp.setMesg(String0.null2EmptyTo(tmp.getMesg(), resp.getMesg()));
      resp.setData(tmp.getData() == null ? resp.getData() : tmp.getData());
    }
    String rtn = OM3.writeValueAsString(resp);
    log.warn(rtn);
    return rtn;
  }
}
