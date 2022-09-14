package sktest.leon.persistence.biz;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.shaneking.leon.persistence.biz.WebPersistenceEntityBiz;
import org.shaneking.ling.rr.Ctx;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.ReqMsg;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.persistence.simple.SimpleTenantEntity;
import org.shaneking.roc.persistence.simple.SimpleUserEntity;
import org.shaneking.roc.test.SKSpringUnit;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class WebPersistenceEntityBizSpringTest extends SKSpringUnit {

  @Autowired
  private WebPersistenceEntityBiz webPersistenceEntityBiz;

  void csv(String fileType) {
    Req<String> req = Req.<String>build().setCtx(new Ctx().setTenant(new SimpleTenantEntity()).setUser(new SimpleUserEntity()))
      .srtObj(tstIFiles(fileType).getAbsolutePath());
    req.setMsg(ReqMsg.<String>build().setTno(UUID0.cUl33()));
    req.gnnCtx().getTenant().setId("skTestTenantId");
    req.gnnCtx().getUser().setId("skTestUserId");
    webPersistenceEntityBiz.csv(req, SimpleUserEntity.class);
  }

  @Test
  void csv1() {
    csv(File0.TYPE_CSV);
  }

  @Test
  void csv2() {
    csv(File0.TYPE_CSV);
  }

  @Test
  void csv3() {
    csv(File0.TYPE_XLSX);
  }
}
