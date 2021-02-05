package org.shaneking.leon.persistence.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.persistence.biz.EntityBiz;
import org.shaneking.ling.cache.StringCaches;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.persistence.GlobalNumbered;
import org.shaneking.ling.persistence.Numbered;
import org.shaneking.ling.persistence.Tenanted;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.Date0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.shaneking.roc.persistence.entity.CacheableEntity;
import org.shaneking.roc.persistence.entity.UserEntity;
import org.shaneking.roc.rr.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EntityBizImpl implements EntityBiz {
  @Value("${sk.leon.biz.file.upload.folder:/tmp}")
  private String uploadFolder;
  @Value("${sk.leon.biz.file.export.folder:/tmp}")
  private String exportFolder;
  @Autowired
  private StringCaches stringCaches;
  @Autowired
  private CacheableDao cacheableDao;
  @Autowired
  private UserEntity userEntity;

  @Override
  public <T extends CacheableEntity> Resp<Req<T, Integer>> mge(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      if (String0.isNullOrEmpty(t.getId())) {
        T existT = null;
        if (t instanceof GlobalNumbered && !String0.isNullOrEmpty(((GlobalNumbered) t).getNo())) {
          T tmpT = entityClass.newInstance();
          ((GlobalNumbered) tmpT).setNo(((GlobalNumbered) t).getNo());
          existT = cacheableDao.one(entityClass, tmpT, true);
        } else if (t instanceof Numbered && t instanceof Tenanted && !String0.isNullOrEmpty(((Numbered) t).getNo()) && !String0.isNullOrEmpty(req.gnnCtx().gnaTenantId())) {
          T tmpT = entityClass.newInstance();
          ((Numbered) tmpT).setNo(((Numbered) t).getNo());
          ((Tenanted) tmpT).setTenantId(req.gnnCtx().gnaTenantId());
          existT = cacheableDao.one(entityClass, tmpT, true);
        }
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
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<T, Integer>> add(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.initWithUserIdAndId(req.gnnCtx().gnaUserId(), UUID0.cUl33());
      req.getPri().setRtn(cacheableDao.add(entityClass, CacheableDao.pti(t, req.gnnCtx().gnaTenantId())));
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<T, Integer>> rmv(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      req.getPri().setRtn(cacheableDao.rmv(entityClass, CacheableDao.ptu(t, req.gnnCtx().gnaTenantId())));
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<T, Integer>> del(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      if (String0.isNullOrEmpty(t.getId())) {
        String ids = cacheableDao.ids(entityClass, CacheableDao.pts(t, req.gnnCtx().gnaTenantId()));
        if (String0.isNullOrEmpty(ids)) {
          req.getPri().setRtn(0);
        } else {
          ///show variables like 'max_allow%';
          ///max_allowed_packet
          req.getPri().setRtn(cacheableDao.delByIds(entityClass, List0.newArrayList(ids.split(String0.COMMA))));
        }
      } else {
        req.getPri().setRtn(cacheableDao.delById(entityClass, CacheableDao.ptu(t, req.gnnCtx().gnaTenantId())));
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<T, Integer>> mod(Req<T, Integer> req, Class<T> entityClass) {
    Resp<Req<T, Integer>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.setLastModifyDateTime(Date0.on().dateTime());
      t.setLastModifyUserId(req.gnnCtx().gnaUserId());
      if (String0.isNullOrEmpty(t.getId())) {
        T tmpT = entityClass.newInstance();
        tmpT.setVersion(t.getVersion()).setWhereConditions(t.getWhereConditions());
        String ids = cacheableDao.ids(entityClass, CacheableDao.pts(tmpT, req.gnnCtx().gnaTenantId()));
        if (String0.isNullOrEmpty(ids)) {
          req.getPri().setRtn(0);
        } else {
          req.getPri().setRtn(cacheableDao.modByIdsVer(entityClass, CacheableDao.ptu(t, req.gnnCtx().gnaTenantId()), List0.newArrayList(ids.split(String0.COMMA))));
        }
      } else {
        req.getPri().setRtn(cacheableDao.modByIdVer(entityClass, CacheableDao.ptu(t, req.gnnCtx().gnaTenantId())));
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<T, List<T>>> lst(Req<T, List<T>> req, Class<T> entityClass) {
    Resp<Req<T, List<T>>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.setPagination(t.getPagination() == null ? req.getPri().gnnExt().gnnTbl().gnnPagination() : t.getPagination());
      t.setLastModifyUser(String0.isNullOrEmpty(t.getLastModifyUserId()) ? null : cacheableDao.oneById(userEntity.entityClass(), t.getLastModifyUserId()));
      req.getPri().setRtn(cacheableDao.lst(entityClass, CacheableDao.pts(t, req.gnnCtx().gnaTenantId())));
      t.getPagination().setCount(cacheableDao.cnt(entityClass, CacheableDao.pts(t, req.gnnCtx().gnaTenantId())));
      req.getPri().gnnExt().gnnTbl().setPagination(t.getPagination());
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<T, T>> one(Req<T, T> req, Class<T> entityClass) {
    Resp<Req<T, T>> resp = Resp.success(req);
    try {
      T t = req.getPri().getObj();
      t.setLastModifyUser(String0.isNullOrEmpty(t.getLastModifyUserId()) ? null : cacheableDao.oneById(userEntity.entityClass(), t.getLastModifyUserId()));
      T rst = cacheableDao.one(entityClass, CacheableDao.pts(t, req.gnnCtx().gnaTenantId()));
      rst.setLastModifyUser((rst.getLastModifyUser() == null && String0.isNullOrEmpty(rst.getLastModifyUserId())) ? cacheableDao.oneById(userEntity.entityClass(), rst.getLastModifyUserId()) : rst.getLastModifyUser());
      req.getPri().setRtn(rst);
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<String, String>> csvAdd(Req<String, String> req, Class<T> entityClass) {
    return null;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<String, String>> csvMod(Req<String, String> req, Class<T> entityClass) {
    return null;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<String, String>> xlsxTmp(Req<String, String> req, Class<T> entityClass) {
    return null;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<String, Integer>> xlsxImp(Req<String, Integer> req, Class<T> entityClass) {
    return null;
  }

  @Override
  public <T extends CacheableEntity> Resp<Req<String, String>> xlsxExp(Req<String, String> req, Class<T> entityClass) {
    return null;
  }
}
