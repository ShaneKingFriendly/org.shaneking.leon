package sktest.leon.rr.q5q5bean.user;

import lombok.NonNull;
import org.shaneking.roc.persistence.hello.HelloUserEntity;

public class WebRrUserBeanPrepare extends HelloUserEntity {
  public static WebRrUserBeanPrepare fromEntity(HelloUserEntity entity) {
    WebRrUserBeanPrepare rtn = null;
    if (entity != null) {
      rtn = new WebRrUserBeanPrepare();
      transform(entity, rtn);
    }
    return rtn;
  }

  public static void transform(@NonNull HelloUserEntity from, @NonNull HelloUserEntity to) {
    to.setId(from.getId());
    to.setNo(from.getNo());
    to.setName(from.getName());
    to.setMobile(from.getMobile());
    to.setEmail(from.getEmail());
  }

  public static HelloUserEntity toEntity(WebRrUserBeanPrepare bean) {
    HelloUserEntity rtn = null;
    if (bean != null) {
      rtn = new HelloUserEntity();
      transform(bean, rtn);
    }
    return rtn;
  }

  public HelloUserEntity toEntity(HelloUserEntity to) {
    HelloUserEntity rtn = to;
    if (rtn == null) {
      rtn = new HelloUserEntity();
    }
    transform(this, rtn);
    return rtn;
  }
}
