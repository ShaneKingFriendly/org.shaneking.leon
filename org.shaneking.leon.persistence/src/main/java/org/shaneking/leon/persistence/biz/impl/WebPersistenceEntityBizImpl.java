package org.shaneking.leon.persistence.biz.impl;

import com.github.liaochong.myexcel.core.CsvBuilder;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.persistence.biz.WebPersistenceEntityBiz;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.persistence.Pagination;
import org.shaneking.ling.persistence.entity.Identified;
import org.shaneking.ling.persistence.entity.sql.Tenanted;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.lang.ZeroException;
import org.shaneking.ling.zero.util.Date0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.persistence.CacheableEntities;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.shaneking.roc.persistence.dao.NumberedCacheableDao;
import org.shaneking.roc.persistence.dao.TenantNumberedCacheableDao;
import org.shaneking.roc.persistence.entity.NumberedEntities;
import org.shaneking.roc.persistence.entity.TenantNumberedEntities;
import org.shaneking.roc.persistence.entity.sql.UserEntities;
import org.shaneking.roc.rr.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class WebPersistenceEntityBizImpl implements WebPersistenceEntityBiz {
  @Value("${sk.leon.persistence.file.temporary.folder:/tmp}")
  private String temporaryFolder;
  @Value("${sk.leon.persistence.file.csv.buffer:1023}")
  private int csvBuffer;
  @Autowired
  private CacheableDao cacheableDao;
  @Autowired
  private NumberedCacheableDao numberedCacheableDao;
  @Autowired
  private TenantNumberedCacheableDao tenantNumberedCacheableDao;
  @Autowired
  private UserEntities userEntityClass;

  private <T extends CacheableEntities> T exists(Class<T> entityClass, T t, String tenantId) throws Exception {
    T rtn = null;
    if (t instanceof TenantNumberedEntities && !String0.isNullOrEmpty(((TenantNumberedEntities) t).getNo()) && !String0.isNullOrEmpty(tenantId)) {
      rtn = (T) tenantNumberedCacheableDao.oneByNo(((TenantNumberedEntities) t).getClass(), ((TenantNumberedEntities) t).getNo(), tenantId, true);
    } else if (t instanceof NumberedEntities && !String0.isNullOrEmpty(((NumberedEntities) t).getNo())) {
      rtn = (T) numberedCacheableDao.oneByNo(((NumberedEntities) t).getClass(), ((NumberedEntities) t).getNo(), true);
    }
    return rtn;
  }

  @Override
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

  @Override
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

  @Override
  public <T extends CacheableEntities> Resp<Req<T, Integer>> rmv(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      if (String0.isNullOrEmpty(t.getId()) && t.findWhereConditions(Identified.FIELD__ID).size() == 0) {
        long cnt = cacheableDao.cnt(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId())));
        if (cnt < Pagination.MAX_SIZE) {
          t.forceWhereCondition(Identified.FIELD__ID).resetVal(cacheableDao.lstIds(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
          resp = rmvById(req, entityClass);
        } else {
          req.getPri().setRtn(cacheableDao.rmv(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
        }
      } else {
        resp = rmvById(req, entityClass);
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntities> Resp<Req<T, Integer>> rmvById(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      if (String0.isNullOrEmpty(t.getId()) && t.findWhereConditions(Identified.FIELD__ID).size() == 0) {
        resp.setCode(Identified.ERR_CODE__REQUIRED);
      } else {
        if (String0.isNullOrEmpty(t.getId())) {
          long cnt = cacheableDao.cnt(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId())));
          if (cnt < Pagination.MAX_SIZE) {
            Pagination definedPagination = t.sroPagination(new Pagination().setPage(Pagination.MAX_SIZE));
            List<String> ids = cacheableDao.lstIds(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId())));
            t.setPagination(definedPagination);
            ///show variables like 'max_allow%';
            ///max_allowed_packet
            req.getPri().setRtn(ids.size() > 0 ? cacheableDao.rmvByIds(entityClass, ids) : 0);
          } else {
            req.getPri().setRtn(cacheableDao.rmv(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
          }
        } else {
          req.getPri().setRtn(cacheableDao.rmvById(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
        }
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntities> Resp<Req<T, Integer>> mod(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.setLastModifyDateTime(Date0.on().dateTime());
      t.setLastModifyUserId(req.gnnCtx().gnaUserId());
      if (String0.isNullOrEmpty(t.getId()) && t.findWhereConditions(Identified.FIELD__ID).size() == 0) {
        long cnt = Pagination.MAX_SIZE;
        T tmpT = entityClass.newInstance();
        tmpT.setPagination(new Pagination().setSize(Pagination.MAX_SIZE));
        if (t.getWhereConditions().size() > 0) {
          tmpT.srvWhereConditions(t.getWhereConditions());
          tmpT.setVersion(t.getVersion());
          cnt = cacheableDao.cnt(entityClass, CacheableDao.pts(tmpT, List0.newArrayList(req.gnnCtx().gnaTenantId())));
        }
        if (cnt < Pagination.MAX_SIZE) {
          t.forceWhereCondition(Identified.FIELD__ID).resetVal(cacheableDao.lstIds(entityClass, CacheableDao.pts(tmpT, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
          resp = rmvById(req, entityClass);
        } else {
          req.getPri().setRtn(cacheableDao.mod(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
        }
      } else {
        resp = modById(req, entityClass);
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntities> Resp<Req<T, Integer>> modById(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      if (String0.isNullOrEmpty(t.getId()) && t.findWhereConditions(Identified.FIELD__ID).size() == 0) {
        resp.setCode(Identified.ERR_CODE__REQUIRED);
      } else {
        t.setLastModifyDateTime(Date0.on().dateTime());
        t.setLastModifyUserId(req.gnnCtx().gnaUserId());
        if (String0.isNullOrEmpty(t.getId())) {
          long cnt = cacheableDao.cnt(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId())));
          if (cnt < Pagination.MAX_SIZE) {
            T tmpT = entityClass.newInstance();
            tmpT.srvWhereConditions(t.getWhereConditions());
            tmpT.setVersion(t.getVersion());
            tmpT.setPagination(new Pagination().setPage(Pagination.MAX_SIZE));
            List<String> ids = cacheableDao.lstIds(entityClass, CacheableDao.pts(tmpT, List0.newArrayList(req.gnnCtx().gnaTenantId())));
            req.getPri().setRtn(ids.size() > 0 ? cacheableDao.modByIdsVer(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId())), ids) : 0);
          } else {
            req.getPri().setRtn(cacheableDao.mod(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
          }
        } else {
          req.getPri().setRtn(cacheableDao.modByIdVer(entityClass, CacheableDao.ptu(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
        }
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntities> Resp<Req<T, List<T>>> lst(Req<T, List<T>> req, Class<T> entityClass) {
    Resp<Req<T, List<T>>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.setLastModifyUser(String0.isNullOrEmpty(t.getLastModifyUserId()) ? null : cacheableDao.oneById(userEntityClass.entityClass(), t.getLastModifyUserId()));
      t.setPagination(t.getPagination() == null ? req.getPri().gnnExt().gnnTbl().gnnPagination() : t.getPagination());
      req.getPri().setRtn(cacheableDao.lst(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
      t.getPagination().setCount(cacheableDao.cnt(entityClass, CacheableDao.pts(t, List0.newArrayList(req.gnnCtx().gnaTenantId()))));
      req.getPri().gnnExt().gnnTbl().setPagination(t.getPagination());
      ///setLastModifyUser in other biz when need
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
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

  @Override
  public <T extends CacheableEntities> Resp<Req<String, String>> csv(Req<String, String> req, Class<T> entityClass) {
    Resp<Req<String, String>> resp = Resp.success(req);
    try {
      List<T> list = List0.newArrayList();
      File csvFile = new File(req.getPri().getObj());
      Path tmpPath = Paths.get(temporaryFolder, String.valueOf(req.gnnCtx().gnaTenantId()), Date0.on().ySmSd(), String0.nullOrEmptyTo(req.getPub().getTracingNo(), UUID0.cUl33()), entityClass.getSimpleName() + File0.suffix(File0.TYPE_CSV));
      SaxExcelReader.of(entityClass).rowFilter(row -> row.getRowNum() > 0).readThen(csvFile, (row, ctx) -> {
        try {
          if (ctx.getRowNum() > 0 && ctx.getRowNum() % csvBuffer == 0) {
            if (ctx.getRowNum() / csvBuffer == 1) {
              CsvBuilder.of(entityClass).build(list).write(tmpPath);
            } else {
              CsvBuilder.of(entityClass).noTitles().build(list).write(tmpPath, true);
            }
            list.clear();
          } else {
            if (row instanceof Tenanted) {
              ((Tenanted) row).setTenantId(req.gnnCtx().gnaTenantId());
            }
            row.initWithUserId(req.gnnCtx().gnaUserId());
            if (String0.isNullOrEmpty(row.getId())) {
              T existT = exists(entityClass, row, req.gnnCtx().gnaTenantId());
              if (existT == null) {
                row.sinId(UUID0.cUl33());
              } else {
                row.setVersion(existT.getVersion());
                row.setId(existT.getId());
              }
            }
            list.add(row);
          }
        } catch (Exception e) {
          log.error(OM3.p(row, ctx), e);
          throw new ZeroException(OM3.p(row, ctx), e);
        }
      });
      CsvBuilder.of(entityClass).noTitles().build(list).write(tmpPath, true);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, entityClass), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
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

  @Override
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
