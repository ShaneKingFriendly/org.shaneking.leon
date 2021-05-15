package sktest.leon.rr.c5c5bean.user;

import lombok.NonNull;
import org.shaneking.roc.persistence.hello.HelloUserEntity;

public class HelloWebRrUserBean extends HelloUserEntity {
  public static HelloWebRrUserBean fromEntity(HelloUserEntity entity) {
    HelloWebRrUserBean rtn = null;
    if (entity != null) {
      rtn = new HelloWebRrUserBean();
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

  public static HelloUserEntity toEntity(HelloWebRrUserBean bean) {
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
