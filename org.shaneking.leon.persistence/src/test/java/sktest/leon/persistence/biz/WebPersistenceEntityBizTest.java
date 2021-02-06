package sktest.leon.persistence.biz;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.shaneking.ling.test.SKUnit;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.roc.persistence.hello.entity.HelloUserEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebPersistenceEntityBizTest extends SKUnit {

  @Test
  void mge() {
  }

  @Test
  void add() {
  }

  @Test
  void rmv() {
  }

  @Test
  void del() {
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

  @Test
  void csv() {
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
