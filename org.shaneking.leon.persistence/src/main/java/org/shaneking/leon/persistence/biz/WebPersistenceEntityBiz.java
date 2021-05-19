package org.shaneking.leon.persistence.biz;

import com.github.liaochong.myexcel.core.CsvBuilder;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.persistence.Pagination;
import org.shaneking.ling.persistence.entity.Identified;
import org.shaneking.ling.persistence.entity.sql.TenantReadable;
import org.shaneking.ling.persistence.entity.sql.Tenanted;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.rr.RespException;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.Char0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.lang.ZeroException;
import org.shaneking.ling.zero.persistence.Tuple;
import org.shaneking.ling.zero.text.MF0;
import org.shaneking.ling.zero.util.Date0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.ling.zero.util.Map0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.persistence.CacheableEntities;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.shaneking.roc.persistence.dao.NumberedCacheableDao;
import org.shaneking.roc.persistence.dao.TenantedNumberedCacheableDao;
import org.shaneking.roc.persistence.entity.NumberedEntities;
import org.shaneking.roc.persistence.entity.ReadableTenantEntities;
import org.shaneking.roc.persistence.entity.TenantedNumberedEntities;
import org.shaneking.roc.persistence.entity.sql.UserEntities;
import org.shaneking.roc.rr.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@ConditionalOnBean(UserEntities.class)
@Service
@Slf4j
public class WebPersistenceEntityBiz {
  @Transient
  public static final String ERR_CODE__ILLEGAL_CHARACTER = "WEB_PERSISTENCE_ENTITY_BIZ__ILLEGAL_CHARACTER(')";
  @Value("${sk.leon.persistence.file.temporary.folder:/tmp}")
  private String temporaryFolder;
  @Value("${sk.leon.persistence.file.csv.buffer:1023}")
  private int csvBuffer;
  @Value("${sk.leon.persistence.dbserver.backup.folder:/tmp}")
  private String dbserverBackupFolder;
  @Autowired
  private CacheableDao cacheableDao;
  @Autowired
  private NumberedCacheableDao numberedCacheableDao;
  @Autowired
  private TenantedNumberedCacheableDao tenantedNumberedCacheableDao;
  @Autowired
  private UserEntities userEntityClass;

  //special for common
  private <T extends CacheableEntities> T exists(Class<T> entityClass, T t, String tenantId) throws Exception {
    T rtn = null;
    if (tenantedNumberedCacheableDao != null && t instanceof TenantedNumberedEntities && !String0.isNullOrEmpty(t.getNo()) && !String0.isNullOrEmpty(tenantId)) {
      rtn = (T) tenantedNumberedCacheableDao.oneByNo(((TenantedNumberedEntities) t).getClass(), t.getNo(), tenantId, true);
    } else if (numberedCacheableDao != null && t instanceof NumberedEntities && !String0.isNullOrEmpty(t.getNo())) {
      rtn = (T) numberedCacheableDao.oneByNo(((NumberedEntities) t).getClass(), t.getNo(), true);
    }
    return rtn;
  }

  private <T extends CacheableEntities> List<String> soft(Req<T, Integer> req, Class<T> entityClass, @NonNull String dd) throws Exception {
    T t = req.getPri().getObj();

    long cnt = cacheableDao.cnt(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId())));
    T modT = t.filedToCondition();
    modT.setDd(dd);
    modT.setLastModifyDateTime(Date0.on().dateTime()).setLastModifyUserId(req.gnnCtx().gnaUserId());
    List<String> ids = List0.newArrayList();
    if (cnt < Pagination.MAX_SIZE) {
      Pagination definedPagination = t.sroPagination(new Pagination().setSize(Pagination.MAX_SIZE));
      ids = cacheableDao.lstIds(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId())));
      req.getPri().setRtn(cacheableDao.modByIdsVer(entityClass, CacheableDao.ptu(modT, List0.newArrayList(req.gnnCtx().gnaTenantId())), ids));
      t.setPagination(definedPagination);

      if (ids.size() != req.getPri().getRtn()) {
        log.warn(OM3.lp(dd, ids.toArray()));

        T idsT = entityClass.newInstance().nullSetter().setPagination(new Pagination().setSize(Pagination.MAX_SIZE));
        idsT.setDd(dd);
        ids = cacheableDao.lstIds(entityClass, CacheableDao.pts(idsT, List0.newArrayList(req.gnnCtx().gnaTenantId())));

        if (ids.size() != req.getPri().getRtn()) {
          log.error(OM3.lp(dd, ids.toArray()));
          ids = List0.newArrayList();
        }
      }
    } else {
      req.getPri().setRtn(cacheableDao.mod(entityClass, CacheableDao.ptu(modT, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
    }

    return ids;
  }

  protected <T extends CacheableEntities> String softSubQuery(Req<T, Integer> req, Class<T> entityClass, @NonNull String dd) throws Exception {
    T subT = entityClass.newInstance();
    subT.nullSetter();
    subT.setDd(dd);
    Tuple.Pair<List<String>, List<Object>> pair = subT.selectSql(List0.newArrayList(Identified.COLUMN__ID), List0.newArrayList(), true);
    return String.join(String0.BLANK, Tuple.getFirst(pair)).replace(String0.QUESTION, String0.wrap(subT.getDd(), String0.SINGLE_QUOTATION));
  }

  public <T extends CacheableEntities> Resp<Req<String, String>> template(Req<String, String> req, Class<T> entityClass) {
    Resp<Req<String, String>> resp = Resp.success(req);
    try {
      T t = entityClass.newInstance();
      Path path = Paths.get(temporaryFolder, String.valueOf(req.gnnCtx().gnaTenantId()), Date0.on().ySmSd(), entityClass.getSimpleName() + File0.suffix(File0.TYPE_XLSX));
      if (!path.toFile().exists()) {
        path.toFile().getParentFile().mkdirs();
        FileExportUtil.export(DefaultExcelBuilder.of(entityClass).build(List0.newArrayList(t)), path.toFile());
      }
      req.getPri().setRtn(path.toFile().getAbsolutePath());
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  //special for rmv
  protected <T extends CacheableEntities> Map<String, Map<String, Integer>> rmvRel(Req<T, Integer> req, Class<T> entityClass, @NonNull String dd, @NonNull List<String> ids) throws Exception {
    ///abstract
    return Map0.newHashMap();
  }

  //special for del
  protected <T extends CacheableEntities> Map<String, Map<String, Integer>> delRel(Req<T, Integer> req, Class<T> entityClass, @NonNull String dd, @NonNull List<String> ids) throws Exception {
    ///abstract
    return Map0.newHashMap();
  }

  //common
  public <T extends CacheableEntities> Resp<Req<T, Integer>> mge(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      if (String0.isNullOrEmpty(t.getId())) {
        T existT = exists(entityClass, t, req.gnnCtx().gnaTenantId());
        if (existT == null) {
          resp = add(req, entityClass);
        } else {
          t.setId(existT.getId());
          resp = mod(req, entityClass);
        }
      } else {
        if (cacheableDao.oneById(entityClass, t.getId(), true) == null) {
          resp = add(req, entityClass);
        } else {
          resp = mod(req, entityClass);
        }
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> add(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.initWithUserIdAndId(req.gnnCtx().gnaUserId(), UUID0.cUl33());
      req.getPri().setRtn(cacheableDao.add(entityClass, CacheableDao.pti(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> rmv(Req<T, Integer> req, Class<T> entityClass) {
    return rmv(req, entityClass, UUID0.cUl33());
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> rmv(Req<T, Integer> req, Class<T> entityClass, @NonNull String dd) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();

      List<String> ids = soft(req, entityClass, dd);

      resp.setMesg(OM3.writeValueAsString(rmvRel(req, entityClass, dd, ids)));

      //create table if not exists tableName_d (like tableName);
      //insert into tableName_d(columns) select columns from tableName where dd = ?
      assert req.getPri().getRtn() == cacheableDao.getJdbcTemplate().update(MessageFormat.format("insert into {0}({1}) select {1} from {2} where dd = ?", t.deletedFullTableName()
        , t.getFieldNameList().stream().map(f -> t.getDbColumnMap().get(f)).collect(Collectors.joining(String0.COMMA)), t.fullTableName()), dd);
      //delete original records
      T rmvT = entityClass.newInstance();
      rmvT.setDd(dd);
      if (ids.size() > 0) {
        assert req.getPri().getRtn() == cacheableDao.rmvByIds(entityClass, CacheableDao.ptu(rmvT, List0.newArrayList(req.gnnCtx().gnaTenantId())), ids);
      } else {
        assert req.getPri().getRtn() == cacheableDao.rmv(entityClass, CacheableDao.ptu(rmvT, List0.newArrayList(req.gnnCtx().gnaTenantId())));
      }

      t.setDd(dd);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> del(Req<T, Integer> req, Class<T> entityClass) {
    return del(req, entityClass, UUID0.cUl33());
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> del(Req<T, Integer> req, Class<T> entityClass, @NonNull String dd) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();

      List<String> ids = soft(req, entityClass, dd);

      resp.setMesg(OM3.writeValueAsString(delRel(req, entityClass, dd, ids)));

      t.setDd(dd);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> ivd(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      T tmpT = t.filedToCondition();
      List<String> ids = List0.newArrayList();
      long cnt = cacheableDao.cnt(entityClass, CacheableDao.pts(tmpT, List0.newArrayList(req.gnnCtx().gnaTenantId())));
      if (cnt < Pagination.MAX_SIZE) {
        Pagination definedPagination = tmpT.sroPagination(new Pagination().setSize(Pagination.MAX_SIZE));
        ids = cacheableDao.lstIds(entityClass, CacheableDao.pts(tmpT, List0.newArrayList(req.gnnCtx().gnaTenantId())));
        tmpT.setPagination(definedPagination);
      }
      tmpT.setInvalid(String0.Y).setLastModifyDateTime(Date0.on().dateTime()).setLastModifyUserId(req.gnnCtx().gnaUserId());
      if (ids.size() > 0) {
        req.getPri().setRtn(cacheableDao.modByIdsVer(entityClass, CacheableDao.ptu(tmpT, List0.newArrayList(req.gnnCtx().gnaTenantId())), ids));
      } else {
        req.getPri().setRtn(cacheableDao.mod(entityClass, CacheableDao.ptu(tmpT, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> mod(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      if (!String0.isNullOrEmpty(t.getId())) {
        req.getPri().setRtn(cacheableDao.modByIdVer(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
      } else {
        List<String> cl = t.forceWhereCondition(Identified.FIELD__ID).getCl();
        if (cl != null && cl.size() > 0) {
          req.getPri().setRtn(cacheableDao.modByIdsVer(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId())), cl));
        } else {
          T idsT = entityClass.newInstance().nullSetter().setPagination(new Pagination().setSize(Pagination.MAX_SIZE));
          idsT.setVersion(t.getVersion());
          idsT.srvWhereConditions(t.getWhereConditions());
          List<String> ids = List0.newArrayList();
          long cnt = cacheableDao.cnt(entityClass, CacheableDao.pts(idsT, List0.newArrayList(req.gnnCtx().gnaTenantId())));
          if (cnt < Pagination.MAX_SIZE) {
            ids = cacheableDao.lstIds(entityClass, CacheableDao.pts(idsT, List0.newArrayList(req.gnnCtx().gnaTenantId())));
          }
          if (ids.size() > 0) {
            req.getPri().setRtn(cacheableDao.modByIdsVer(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId())), ids));
          } else {
            req.getPri().setRtn(cacheableDao.mod(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
          }
        }
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, List<T>>> lst(Req<T, List<T>> req, Class<T> entityClass) {
    Resp<Req<T, List<T>>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.setLastModifyUser(String0.isNullOrEmpty(t.getLastModifyUserId()) ? null : cacheableDao.oneById(userEntityClass.entityClass(), t.getLastModifyUserId()));
      t.setPagination(t.getPagination() == null ? req.getPri().gnnExt().gnnTbl().gnnPagination() : t.getPagination());
      List<String> tenantIdList = List0.newArrayList(req.gnnCtx().gnaTenantId());
      if (t instanceof TenantReadable) {
        tenantIdList = ReadableTenantEntities.calc(req.gnnCtx().getTrtList(), entityClass.getName(), List0.newArrayList(req.gnnCtx().gnaTenantId()));
      }
      req.getPri().setRtn(cacheableDao.lst(entityClass, CacheableDao.pts(t, tenantIdList)));
      t.getPagination().setCount(cacheableDao.cnt(entityClass, CacheableDao.pts(t, tenantIdList)));
      req.getPri().gnnExt().gnnTbl().setPagination(t.getPagination());
      ///setLastModifyUser in other biz when need
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, T>> one(Req<T, T> req, Class<T> entityClass) {
    Resp<Req<T, T>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.setLastModifyUser(String0.isNullOrEmpty(t.getLastModifyUserId()) ? null : cacheableDao.oneById(userEntityClass.entityClass(), t.getLastModifyUserId()));
      T rst = cacheableDao.one(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId())));
      rst.setLastModifyUser((Objects.equals(rst.getLastModifyUserId(), t.getLastModifyUserId())) ? t.getLastModifyUser() : cacheableDao.oneById(userEntityClass.entityClass(), rst.getLastModifyUserId()));
      req.getPri().setRtn(rst);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<T, String>> iof(Req<T, String> req, Class<T> entityClass) {
    Resp<Req<T, String>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      String rtn = dbserverBackupFolder + String0.SLASH + String0.nullOrEmptyTo(req.gnnCtx().gnaAuditId(), UUID0.cUl33());
      Tuple.Pair<List<String>, List<Object>> pair = t.selectSql(t.getSelectList(), List0.newArrayList(), true);
      List<String> args = List0.newArrayList();
      for (Object o : Tuple.getSecond(pair)) {
        if (String.valueOf(o).contains(String0.SINGLE_QUOTATION)) {
          throw new RespException(Resp.failed(ERR_CODE__ILLEGAL_CHARACTER, String.valueOf(o), req));
        }
        args.add(String0.wrap(String.valueOf(o), o instanceof String ? String0.SINGLE_QUOTATION : String0.EMPTY));
      }
      String sql = String.format(String.join(String0.BLANK, Tuple.getFirst(pair)).replace(String0.QUESTION, "%s"), args)
        + "into outfile '" + rtn + "' fields terminated by ',' optionally enclosed by '\"' lines terminated by '\\n'";
      log.info(sql);
      cacheableDao.getJdbcTemplate().execute(sql);
      req.getPri().setRtn(rtn);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<String, String>> csv(Req<String, String> req, Class<T> entityClass) {
    Resp<Req<String, String>> resp = Resp.success(req);
    try {
      AtomicInteger writeTimes = new AtomicInteger(0);
      List<T> list = List0.newArrayList();
      File csvFile = new File(req.getPri().getObj());
      Path tmpPath = Paths.get(temporaryFolder, String.valueOf(req.gnnCtx().gnaTenantId()), Date0.on().ySmSd(), String0.nullOrEmptyTo(req.getPub().getTracingNo(), UUID0.cUl33()), entityClass.getSimpleName() + File0.suffix(File0.TYPE_CSV));
      tmpPath.toFile().getParentFile().mkdirs();
      SaxExcelReader.of(entityClass).rowFilter(row -> row.getRowNum() > 0).readThen(csvFile, (row, ctx) -> {
        try {
          if (row instanceof Tenanted) {
            ((Tenanted) row).setTenantId(req.gnnCtx().gnaTenantId());
          }
          row.initWithUserIdAndId(req.gnnCtx().gnaUserId(), UUID0.cUl33());
          list.add(row);
          if (ctx.getRowNum() % csvBuffer == 0) {
            if (writeTimes.getAndIncrement() == 0) {
              CsvBuilder.of(entityClass).build(list).write(tmpPath);
            } else {
              CsvBuilder.of(entityClass).noTitles().build(list).write(tmpPath, true);
            }
            list.clear();
          }
        } catch (Exception e) {
          log.error(OM3.p(row, ctx), e);
          throw new ZeroException(OM3.p(row, ctx), e);
        }
      });
      if (list.size() > 0) {
        if (writeTimes.getAndIncrement() == 0) {
          CsvBuilder.of(entityClass).build(list).write(tmpPath);
        } else {
          CsvBuilder.of(entityClass).noTitles().build(list).write(tmpPath, true);
        }
      }
      if (writeTimes.intValue() > 0) {
        String titles = null;
        try (Stream<String> stream = Files.lines(tmpPath)) {
          titles = stream.findFirst().get();
        } catch (Exception e) {
          throw e;
        }
        if (String0.isNullOrEmpty(titles)) {
          resp.setCode(Resp.CODE_UNKNOWN_EXCEPTION);
        } else {
          //\ufeff
          while (titles.length() > 0 && !Char0.isAlphabetOrDigital(titles.charAt(0))) {
            titles = titles.substring(1);
          }
          String sql = MF0.fmt("load data local infile '{0}' replace into table {1} fields terminated by ',' optionally enclosed by '\"' lines terminated by '\\n' ignore 1 lines ({2})"
            , tmpPath.toFile().getAbsolutePath().replaceAll("\\\\", "/")
            , entityClass.newInstance().fullTableName()
            , List0.newArrayList(titles.split(String0.COMMA)).stream().map(s -> String0.wrap(String0.field2DbColumn(s.trim()), "`")).collect(Collectors.joining(String0.COMMA)));
          log.info(sql);
          cacheableDao.getJdbcTemplate().execute(sql);
          req.getPri().setRtn(String0.Y);
        }
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Req<String, Integer>> xlsx(Req<String, Integer> req, Class<T> entityClass) {
    Resp<Req<String, Integer>> resp = Resp.success(req);
    try {
      req.getPri().setRtn(0);
      T entity = entityClass.newInstance();
      File xlsxFile = new File(req.getPri().getObj());
      SaxExcelReader.of(entityClass).sheet(String0.maxLenStr(entity.getDbTableName(), 31)).rowFilter(row -> row.getRowNum() > 0).readThen(xlsxFile, row -> {
        try {
          row.initWithUserId(req.gnnCtx().gnaUserId());
          if (String0.isNullOrEmpty(row.getId())) {
            T existT = exists(entityClass, row, req.gnnCtx().gnaTenantId());
            if (existT == null) {
              row.sinId(UUID0.cUl33());
              req.getPri().setRtn(req.getPri().getRtn() + cacheableDao.add(entityClass, CacheableDao.pti(row, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
            } else {
              row.setVersion(existT.getVersion());
              row.setId(existT.getId());
              req.getPri().setRtn(req.getPri().getRtn() + cacheableDao.modByIdVer(entityClass, CacheableDao.ptu(row, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
            }
          } else {
            if (cacheableDao.oneById(entityClass, row.getId(), true) == null) {
              req.getPri().setRtn(req.getPri().getRtn() + cacheableDao.add(entityClass, CacheableDao.pti(row, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
            } else {
              req.getPri().setRtn(req.getPri().getRtn() + cacheableDao.modByIdVer(entityClass, CacheableDao.ptu(row, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
            }
          }
        } catch (Exception e) {
          log.error(OM3.writeValueAsString(row), e);
          throw new ZeroException(OM3.writeValueAsString(row), e);
        }
      });
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }
}
