package sktest.leon.persistence.biz;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.test.SKUnit;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.roc.persistence.simple.SimpleUserEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class WebPersistenceEntityBizTest extends SKUnit {

  void csv(String fileType) {
    SaxExcelReader.of(SimpleUserEntity.class).rowFilter(row -> row.getRowNum() > 0).readThen(tstIFiles(fileType), (row, ctx) -> {
      log.info(OM3.p(row, ctx));
    });
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
  void template() throws IOException {
    FileExportUtil.export(DefaultExcelBuilder.of(SimpleUserEntity.class).sheetName(new SimpleUserEntity().getDbTableName()).build(List0.newArrayList(new SimpleUserEntity())), tstOFiles(File0.TYPE_XLSX));
    assertEquals(String0.ARY_DEC, String0.DIGITAL);
  }
}
