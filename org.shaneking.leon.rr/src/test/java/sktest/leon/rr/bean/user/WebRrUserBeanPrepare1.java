package sktest.leon.rr.bean.user;

import lombok.NonNull;
import org.shaneking.roc.persistence.simple.SimpleUserEntity;

public class WebRrUserBeanPrepare1 extends SimpleUserEntity {
  public static WebRrUserBeanPrepare1 fromEntity(SimpleUserEntity entity) {
    WebRrUserBeanPrepare1 rtn = null;
    if (entity != null) {
      rtn = new WebRrUserBeanPrepare1();
      transform(entity, rtn);
    }
    return rtn;
  }

  public static SimpleUserEntity toEntity(WebRrUserBeanPrepare1 bean) {
    SimpleUserEntity rtn = null;
    if (bean != null) {
      rtn = new SimpleUserEntity();
      transform(bean, rtn);
    }
    return rtn;
  }

  public static void transform(@NonNull SimpleUserEntity from, @NonNull SimpleUserEntity to) {
    to.setId(from.getId());
    to.setNo(from.getNo());
    to.setName(from.getName());
    to.setMobile(from.getMobile());
    to.setEmail(from.getEmail());
  }

  public SimpleUserEntity toEntity(SimpleUserEntity to) {
    SimpleUserEntity rtn = to;
    if (rtn == null) {
      rtn = new SimpleUserEntity();
    }
    transform(this, rtn);
    return rtn;
  }
}
