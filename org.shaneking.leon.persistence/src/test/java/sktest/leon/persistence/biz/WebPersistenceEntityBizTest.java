package sktest.leon.persistence.biz;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.shaneking.leon.persistence.biz.WebPersistenceEntityBiz;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.persistence.hello.entity.HelloTenantEntity;
import org.shaneking.roc.persistence.hello.entity.HelloUserEntity;
import org.shaneking.roc.rr.Ctx;
import org.shaneking.roc.rr.Pri;
import org.shaneking.roc.rr.Pub;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.test.SKSpringUnit;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class WebPersistenceEntityBizTest extends SKSpringUnit {

  @Autowired
  private WebPersistenceEntityBiz webPersistenceEntityBiz;

  @Test
  void mge() {
  }

  @Test
  void add() {
  }

  @Test
  void del() {
  }

  @Test
  void rmv() {
    assert 1 == new Integer(1);

    boolean exception = false;
    try {
      assert 1 == new Integer(2);
    } catch (Error e) {
      log.error(e.getMessage(), e);
      exception = true;
    }
    assert exception;
  }

  @Test
  void mod() {
  }

  @Test
  void lst() {
  }

  @Test
  void one() {
  }

  void csv(String fileType) {
    Req<String, String> req = Req.<String, String>build().setCtx(new Ctx().setTenant(new HelloTenantEntity()).setUser(new HelloUserEntity()))
      .setPub(new Pub().setTracingNo(UUID0.cUl33()))
      .setPri(Pri.build("", tstIFiles(fileType).getAbsolutePath()));
    req.gnnCtx().getTenant().setId("skTestTenantId");
    req.gnnCtx().getUser().setId("skTestUserId");
    webPersistenceEntityBiz.csv(req, HelloUserEntity.class);
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

  @Test
  void template() throws IOException {
    FileExportUtil.export(DefaultExcelBuilder.of(HelloUserEntity.class).sheetName(new HelloUserEntity().getDbTableName()).build(Lists.newArrayList(new HelloUserEntity())), tstOFiles(File0.TYPE_XLSX));
    assertEquals(String0.ARY_DEC, String0.DIGITAL);
  }

  @Test
  void xlsx() {
  }
}
