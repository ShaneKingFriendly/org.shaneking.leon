package sktest.leon.rr.bean;

import lombok.NonNull;
import org.shaneking.roc.persistence.hello.entity.HelloUserEntity;

public class HelloUserBean extends HelloUserEntity {
  public static HelloUserBean fromEntity(HelloUserEntity entity) {
    HelloUserBean rtn = null;
    if (entity != null) {
      rtn = new HelloUserBean();
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

  public static HelloUserEntity toEntity(HelloUserBean bean) {
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
