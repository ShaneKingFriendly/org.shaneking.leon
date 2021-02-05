package sktest.leon.persistence.biz;

import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import org.shaneking.ling.persistence.sql.entity.sqllite.SqlliteDialectSqlEntities;
import org.shaneking.roc.persistence.entity.UserEntity;

import javax.persistence.Table;

@ExcelModel(includeAllField = false, useFieldNameAsTitle = true)
@Table
public class HelloExcelUserEntity extends UserEntity implements SqlliteDialectSqlEntities {
  @Override
  public Class<? extends HelloExcelUserEntity> entityClass() {
    return HelloExcelUserEntity.class;
  }
}
