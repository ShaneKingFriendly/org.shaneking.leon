package org.shaneking.leon.persistence.biz;

import com.github.liaochong.myexcel.core.CsvBuilder;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.persistence.dao.ProtectDao;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.persistence.CacheableEntities;
import org.shaneking.ling.persistence.entity.Identified;
import org.shaneking.ling.persistence.entity.NumberedUniIdx;
import org.shaneking.ling.persistence.entity.TenantedResourceAccessibleEntities;
import org.shaneking.ling.persistence.entity.sql.*;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.rr.RespException;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.Char0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.lang.ZeroException;
import org.shaneking.ling.zero.persistence.Pagination;
import org.shaneking.ling.zero.persistence.Tuple;
import org.shaneking.ling.zero.text.MF0;
import org.shaneking.ling.zero.time.LD0;
import org.shaneking.ling.zero.time.ZDT0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.ling.zero.util.Map0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.shaneking.roc.persistence.dao.ChannelizedNumberedDao;
import org.shaneking.roc.persistence.dao.NumberedDao;
import org.shaneking.roc.persistence.dao.TenantedNumberedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  @Value("${sk.leon.persistence.file.csv.buffer:1023}")
  private int csvBuffer;
  @Value("${sk.leon.persistence.dbserver.backup.folder:/tmp}")
  private String dbserverBackupFolder;
  @Value("${sk.leon.persistence.file.temporary.folder:/tmp}")
  private String temporaryFolder;
  @Autowired
  private CacheableDao cacheableDao;
  @Autowired
  private ChannelizedNumberedDao channelizedNumberedDao;
  @Autowired
  private NumberedDao numberedDao;
  @Autowired
  private ProtectDao protectDao;
  @Autowired
  private TenantedNumberedDao tenantedNumberedDao;
  @Autowired
  private UserEntities userEntityClass;

  public <T extends CacheableEntities> Resp<Integer, Req<T>> add(Req<T> req, Class<T> entityClass) {
    Resp<Integer, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();
      t.initWithUidAndId(req.gnnCtx().gnaUserId(), UUID0.cUl33());
      resp.srtMsgBodyData(protectDao.add(entityClass, t, req.gnnCtx()));
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  ///must UTF-8, \n
  public <T extends CacheableEntities> Resp<String, Req<String>> csv(Req<String> req, Class<T> entityClass) {
    Resp<String, Req<String>> resp = Resp.success(req, null);
    try {
      AtomicInteger writeTimes = new AtomicInteger(0);
      List<T> list = List0.newArrayList();
      File csvFile = new File(req.gnaMsgBdyObj());
      Path tmpPath = Paths.get(temporaryFolder, String.valueOf(req.gnnCtx().gnaTenantId()), LD0.on().ySmSd(), req.gnnMsg().gnnRno(), req.gnnCtx().gnaAuditId(), entityClass.getSimpleName() + File0.suffix(File0.TYPE_CSV));
      tmpPath.toFile().getParentFile().mkdirs();
      SaxExcelReader.of(entityClass).rowFilter(row -> row.getRowNum() > 0).readThen(csvFile, (row, ctx) -> {
        try {
          if (row instanceof Tenanted) {
            ((Tenanted) row).setTenantId(req.gnnCtx().gnaTenantId());
          }
          row.initWithUidAndId(req.gnnCtx().gnaUserId(), UUID0.cUl33());
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
          resp.srtMsgBodyCode(Resp.CODE_UNKNOWN_EXCEPTION);
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
          resp.srtMsgBodyData(String0.Y);
        }
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Integer, Req<T>> del(Req<T> req, Class<T> entityClass) {
    return del(req, entityClass, UUID0.cUl33());
  }

  public <T extends CacheableEntities> Resp<Integer, Req<T>> del(Req<T> req, Class<T> entityClass, @NonNull String dd) {
    Resp<Integer, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();

      List<String> ids = ddIds(resp, entityClass, dd);

      resp.srtMsgBodyInfo(OM3.writeValueAsString(delRel(req, entityClass, dd, ids)));

      t.setDd(dd);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  //into out file
  public <T extends CacheableEntities> Resp<String, Req<T>> iof(Req<T> req, Class<T> entityClass) {
    Resp<String, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();
      String rtn = dbserverBackupFolder + String0.SLASH + String0.nullOrEmptyTo(req.gnnCtx().gnaAuditId(), UUID0.cUl33());
      Tuple.Pair<List<String>, List<Object>> pair = t.selectSql(t.getSelectList(), List0.newArrayList(), true);
      List<String> args = List0.newArrayList();
      for (Object o : Tuple.getSecond(pair)) {
        if (String.valueOf(o).contains(String0.SINGLE_QUOTATION)) {
          throw new RespException(Resp.failed(req, ERR_CODE__ILLEGAL_CHARACTER, String.valueOf(o), req));
        }
        args.add(String0.wrap(String.valueOf(o), o instanceof String ? String0.SINGLE_QUOTATION : String0.EMPTY));
      }
      String sql = String.format(String.join(String0.BLANK, Tuple.getFirst(pair)).replace(String0.QUESTION, "%s"), args)
        + "into outfile '" + rtn + "' fields terminated by ',' optionally enclosed by '\"' lines terminated by '\\n'";
      log.info(sql);
      cacheableDao.getJdbcTemplate().execute(sql);
      resp.srtMsgBodyData(rtn);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Integer, Req<T>> ivd(Req<T> req, Class<T> entityClass) {
    Resp<Integer, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();
      T tmpT = t.filedToCondition();
      List<String> ids = List0.newArrayList();
      long cnt = protectDao.cnt(entityClass, tmpT, req.gnnCtx());
      if (cnt < Pagination.MAX_SIZE) {
        Pagination definedPagination = tmpT.sroPagination(new Pagination().setRows(Pagination.MAX_SIZE));
        ids = protectDao.lstIds(entityClass, tmpT, req.gnnCtx());
        tmpT.setPagination(definedPagination);
      }
      tmpT.setIvd(String0.Y).setLmDsz(ZDT0.on().dTSZ()).setLmUid(req.gnnCtx().gnaUserId());
      if (ids.size() > 0) {
        resp.srtMsgBodyData(protectDao.modByIdsVer(entityClass, tmpT, ids, req.gnnCtx()));
      } else {
        resp.srtMsgBodyData(protectDao.mod(entityClass, tmpT, req.gnnCtx()));
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<List<T>, Req<T>> lst(Req<T> req, Class<T> entityClass) {
    Resp<List<T>, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();
      t.setLastModifyUser(String0.isNullOrEmpty(t.getLmUid()) ? null : cacheableDao.oneById(userEntityClass.entityClass(), t.getLmUid()));
      t.setPagination(t.getPagination() == null ? req.gnnMsg().gnnBdy().gnnTbl().gnnPage() : t.getPagination());
      List<String> tenantIdList = List0.newArrayList(req.gnnCtx().gnaTenantId());
      if (t instanceof TenantUsable) {
        tenantIdList = TenantedResourceAccessibleEntities.calc(req.gnnCtx().getTutList(), entityClass.getName(), tenantIdList);
      }
      resp.srtMsgBodyData(cacheableDao.lst(entityClass, CacheableDao.pts(t, tenantIdList)));
      t.getPagination().setCount(cacheableDao.cnt(entityClass, CacheableDao.pts(t, tenantIdList)));
      req.gnnMsg().gnnBdy().gnnTbl().setPage(t.getPagination());
      ///setLastModifyUser in other biz when need
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Integer, Req<T>> mge(Req<T> req, Class<T> entityClass) {
    Resp<Integer, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();
      if (String0.isNullOrEmpty(t.getId())) {
        T existT = oneByNo(entityClass, t, req.gnnCtx().gnaTenantId(), req.gnnCtx().gnaChannelId());
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

  public <T extends CacheableEntities> Resp<Integer, Req<T>> mod(Req<T> req, Class<T> entityClass) {
    Resp<Integer, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();
      if (!String0.isNullOrEmpty(t.getId())) {
        resp.srtMsgBodyData(protectDao.modByIdVer(entityClass, t, req.gnnCtx()));
      } else {
        List<String> cl = t.forceWhereCondition(Identified.FIELD__ID).getCl();
        if (cl != null && cl.size() > 0) {
          resp.srtMsgBodyData(protectDao.modByIdsVer(entityClass, t, cl, req.gnnCtx()));
        } else {
          T idsT = entityClass.newInstance().nullSetter().setPagination(new Pagination().setRows(Pagination.MAX_SIZE));
          idsT.setVer(t.getVer());
          idsT.srvWhereConditions(t.getWhereConditions());
          List<String> ids = List0.newArrayList();
          long cnt = protectDao.cnt(entityClass, idsT, req.gnnCtx());
          if (cnt < Pagination.MAX_SIZE) {
            ids = protectDao.lstIds(entityClass, idsT, req.gnnCtx());
          }
          if (ids.size() > 0) {
            resp.srtMsgBodyData(protectDao.modByIdsVer(entityClass, t, ids, req.gnnCtx()));
          } else {
            resp.srtMsgBodyData(protectDao.mod(entityClass, t, req.gnnCtx()));
          }
        }
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<T, Req<T>> one(Req<T> req, Class<T> entityClass) {
    Resp<T, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();
      t.setLastModifyUser(String0.isNullOrEmpty(t.getLmUid()) ? null : cacheableDao.oneById(userEntityClass.entityClass(), t.getLmUid()));
      T rst = protectDao.one(entityClass, t, req.gnnCtx());
      rst.setLastModifyUser((Objects.equals(rst.getLmUid(), t.getLmUid())) ? t.getLastModifyUser() : cacheableDao.oneById(userEntityClass.entityClass(), rst.getLmUid()));
      resp.srtMsgBodyData(rst);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Integer, Req<T>> rmv(Req<T> req, Class<T> entityClass) {
    return rmv(req, entityClass, UUID0.cUl33());
  }

  public <T extends CacheableEntities> Resp<Integer, Req<T>> rmv(Req<T> req, Class<T> entityClass, @NonNull String dd) {
    Resp<Integer, Req<T>> resp = Resp.success(req, null);
    try {
      T t = req.gnaMsgBdyObj();

      List<String> ids = ddIds(resp, entityClass, dd);

      resp.srtMsgBodyInfo(OM3.writeValueAsString(rmvRel(resp, entityClass, dd, ids)));

      //create table if not exists tableName_d (like tableName);
      //insert into tableName_d(columns) select columns from tableName where dd = ?
      assert resp.gnaMsgBodyData() == cacheableDao.getJdbcTemplate().update(MF0.fmt("insert into {0}({1}) select {1} from {2} where dd = ?", t.deletedFullTableName()
        , t.getFieldNameList().stream().map(f -> t.getDbColumnMap().get(f)).collect(Collectors.joining(String0.COMMA)), t.fullTableName()), dd);
      //delete original records
      T rmvT = entityClass.newInstance();
      rmvT.setDd(dd);
      if (ids.size() > 0) {
        assert resp.gnaMsgBodyData() == protectDao.rmvByIds(entityClass, rmvT, ids, req.gnnCtx());
      } else {
        assert resp.gnaMsgBodyData() == protectDao.rmv(entityClass, rmvT, req.gnnCtx());
      }

      t.setDd(dd);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<String, Req<String>> tpl(Req<String> req, Class<T> entityClass) {
    Resp<String, Req<String>> resp = Resp.success(req, null);
    try {
      T t = entityClass.newInstance();
      Path path = Paths.get(temporaryFolder, String.valueOf(req.gnnCtx().gnaTenantId()), LD0.on().ySmSd(), entityClass.getSimpleName() + File0.suffix(File0.TYPE_XLSX));
      if (!path.toFile().exists()) {
        path.toFile().getParentFile().mkdirs();
        FileExportUtil.export(DefaultExcelBuilder.of(entityClass).build(List0.newArrayList(t)), path.toFile());
      }
      resp.srtMsgBodyData(path.toFile().getAbsolutePath());
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public <T extends CacheableEntities> Resp<Integer, Req<String>> xlsx(Req<String> req, Class<T> entityClass) {
    Resp<Integer, Req<String>> resp = Resp.success(req, null);
    try {
      resp.srtMsgBodyData(0);
      T entity = entityClass.newInstance();
      File xlsxFile = new File(req.gnaMsgBdyObj());
      SaxExcelReader.of(entityClass).sheet(String0.maxLenStr(entity.getDbTableName(), 31)).rowFilter(row -> row.getRowNum() > 0).readThen(xlsxFile, row -> {
        try {
          row.initWithUid(req.gnnCtx().gnaUserId());
          if (String0.isNullOrEmpty(row.getId())) {
            T existT = oneByNo(entityClass, row, req.gnnCtx().gnaTenantId(), req.gnnCtx().gnaChannelId());
            if (existT == null) {
              row.sinId(UUID0.cUl33());
              resp.srtMsgBodyData(resp.gnaMsgBodyData() + protectDao.add(entityClass, row, req.gnnCtx()));
            } else {
              row.setVer(existT.getVer());
              row.setId(existT.getId());
              resp.srtMsgBodyData(resp.gnaMsgBodyData() + protectDao.modByIdVer(entityClass, row, req.gnnCtx()));
            }
          } else {
            if (cacheableDao.oneById(entityClass, row.getId(), true) == null) {
              resp.srtMsgBodyData(resp.gnaMsgBodyData() + protectDao.add(entityClass, row, req.gnnCtx()));
            } else {
              resp.srtMsgBodyData(resp.gnaMsgBodyData() + protectDao.modByIdVer(entityClass, row, req.gnnCtx()));
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

  protected <T extends CacheableEntities> Map<String, Map<String, Integer>> delRel(Req<T> req, Class<T> entityClass, @NonNull String dd, @NonNull List<String> ids) throws Exception {
    ///abstract
    return Map0.newHashMap();
  }

  protected <T extends CacheableEntities> String idSubQuery(Req<T> req, Class<T> entityClass, @NonNull String dd) throws Exception {
    T subT = entityClass.newInstance();
    subT.nullSetter();
    subT.setDd(dd);
    Tuple.Pair<List<String>, List<Object>> pair = subT.selectSql(List0.newArrayList(Identified.COLUMN__ID), List0.newArrayList(), true);
    return String.join(String0.BLANK, Tuple.getFirst(pair)).replace(String0.QUESTION, String0.wrap(subT.getDd(), String0.SINGLE_QUOTATION));
  }

  protected <T extends CacheableEntities> Map<String, Map<String, Integer>> rmvRel(Resp<Integer, Req<T>> resp, Class<T> entityClass, @NonNull String dd, @NonNull List<String> ids) throws Exception {
    ///abstract
    return Map0.newHashMap();
  }

  private <T extends CacheableEntities> List<String> ddIds(Resp<Integer, Req<T>> resp, Class<T> entityClass, @NonNull String dd) throws Exception {
    Req<T> req = resp.getReq();
    T t = req.gnaMsgBdyObj();

    long cnt = protectDao.cnt(entityClass, t, req.gnnCtx());
    T modT = t.filedToCondition();
    modT.setDd(dd);
    modT.setLmDsz(ZDT0.on().dTSZ()).setLmUid(req.gnnCtx().gnaUserId());
    List<String> ids = List0.newArrayList();
    if (cnt < Pagination.MAX_SIZE) {
      Pagination definedPagination = t.sroPagination(new Pagination().setRows(Pagination.MAX_SIZE));
      ids = protectDao.lstIds(entityClass, t, req.gnnCtx());
      resp.srtMsgBodyData(protectDao.modByIdsVer(entityClass, modT, ids, req.gnnCtx()));
      t.setPagination(definedPagination);

      if (ids.size() != resp.gnaMsgBodyData()) {
        log.warn(OM3.lp(dd, ids.toArray()));

        T idsT = entityClass.newInstance().nullSetter().setPagination(new Pagination().setRows(Pagination.MAX_SIZE));
        idsT.setDd(dd);
        ids = protectDao.lstIds(entityClass, idsT, req.gnnCtx());

        if (ids.size() != resp.gnaMsgBodyData()) {
          log.error(OM3.lp(dd, ids.toArray()));
          ids = List0.newArrayList();
        }
      }
    } else {
      resp.srtMsgBodyData(protectDao.mod(entityClass, modT, req.gnnCtx()));
    }

    return ids;
  }

  private <T extends CacheableEntities> T oneByNo(Class<T> entityClass, T t, String tenantId, String channelId) {
    T rtn = null;
    if (channelizedNumberedDao != null && t instanceof ChannelizedNumberedUniIdx && !String0.isNullOrEmpty(t.getNo()) && !String0.isNullOrEmpty(channelId)) {
      rtn = channelizedNumberedDao.oneByNo(entityClass, t.getNo(), channelId, true);
    } else if (tenantedNumberedDao != null && t instanceof TenantedNumberedUniIdx && !String0.isNullOrEmpty(t.getNo()) && !String0.isNullOrEmpty(tenantId)) {
      rtn = tenantedNumberedDao.oneByNo(entityClass, t.getNo(), tenantId, true);
    } else if (numberedDao != null && t instanceof NumberedUniIdx && !String0.isNullOrEmpty(t.getNo())) {
      rtn = numberedDao.oneByNo(entityClass, t.getNo(), true);
    }
    return rtn;
  }
}
