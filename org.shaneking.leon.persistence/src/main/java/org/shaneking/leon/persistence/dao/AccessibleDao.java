package org.shaneking.leon.persistence.dao;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.persistence.Entities;
import org.shaneking.ling.persistence.Pagination;
import org.shaneking.ling.persistence.entity.Identified;
import org.shaneking.ling.persistence.entity.sql.TenantReadable;
import org.shaneking.ling.persistence.entity.sql.TenantUsable;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.rr.RespException;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.lang.ZeroException;
import org.shaneking.ling.zero.text.MF0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.roc.persistence.CacheableEntities;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.shaneking.roc.persistence.entity.TenantedResourceAccessibleEntities;
import org.shaneking.roc.rr.Ctx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class AccessibleDao {
  @Autowired
  @Getter
  private CacheableDao cacheableDao;

  public static <T extends CacheableEntities> T pts(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    List<String> tenantIdList = List0.newArrayList(ctx.gnaTenantId());
    if (t instanceof TenantReadable) {
      tenantIdList = TenantedResourceAccessibleEntities.calc(ctx.getTrtList(), cacheType.getName(), tenantIdList);
    }
    if (t instanceof TenantUsable) {
      tenantIdList = TenantedResourceAccessibleEntities.calc(ctx.getTutList(), cacheType.getName(), tenantIdList);
    }
    return CacheableDao.pts(t, tenantIdList);
  }

  public <T extends CacheableEntities> List<T> lst(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx, boolean exceptionIfInvalid) {
    List<T> rtn = cacheableDao.lst(cacheType, pts(cacheType, t, ctx));
    if (exceptionIfInvalid) {
      for (T rt : rtn) {
        if (String0.Y.equals(rt.getIvd())) {
          throw new RespException(Resp.failed(Entities.ERR_CODE__INVALID, MF0.fmt("{0}({1}[{2}]) is invalid", cacheType.getSimpleName(), rt.getNo(), rt.getId())));
        }
      }
    }
    return rtn;
  }

  public <T extends CacheableEntities> List<T> lstByIds(@NonNull Class<T> cacheType, @NonNull List<String> ids, @NonNull Ctx ctx) {
    try {
      T t = cacheType.newInstance();
      t.forceWhereCondition(Identified.FIELD__ID).resetVal(ids);
      return lst(cacheType, t, ctx, false);
    } catch (InstantiationException | IllegalAccessException e) {
      log.error(OM3.p(cacheType, ids, ctx), e);
      throw new ZeroException(e);
    }
  }

  public <T extends CacheableEntities> List<T> lstEii(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return lst(cacheType, t, ctx, true);
  }

  public <T extends CacheableEntities> List<T> lstEiiByIds(@NonNull Class<T> cacheType, @NonNull List<String> ids, @NonNull Ctx ctx) {
    try {
      T t = cacheType.newInstance();
      t.forceWhereCondition(Identified.FIELD__ID).resetVal(ids);
      t.setPagination(new Pagination().setSize(ids.size()));
      return lstEii(cacheType, t, ctx);
    } catch (InstantiationException | IllegalAccessException e) {
      log.error(OM3.p(cacheType, ids, ctx), e);
      throw new ZeroException(e);
    }
  }

  public <T extends CacheableEntities> List<T> lstValid(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    t.setIvd(String0.N);
    return lst(cacheType, t, ctx, false);
  }

  public <T extends CacheableEntities> List<T> lstValidByIds(@NonNull Class<T> cacheType, @NonNull List<String> ids, @NonNull Ctx ctx) {
    try {
      T t = cacheType.newInstance();
      t.forceWhereCondition(Identified.FIELD__ID).resetVal(ids);
      return lstValid(cacheType, t, ctx);
    } catch (InstantiationException | IllegalAccessException e) {
      log.error(OM3.p(cacheType, ids, ctx), e);
      throw new ZeroException(e);
    }
  }

  public <T extends CacheableEntities> T one(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx, boolean rtnNullIfNotEqualsOne, boolean exceptionIfInvalid) {
    T rtn = cacheableDao.one(cacheType, pts(cacheType, t, ctx), rtnNullIfNotEqualsOne);
    if (rtn != null && exceptionIfInvalid && String0.Y.equals(rtn.getIvd())) {
      throw new RespException(Resp.failed(Entities.ERR_CODE__INVALID, MF0.fmt("{0}({1}[{2}]) is invalid", cacheType.getSimpleName(), rtn.getNo(), rtn.getId())));
    }
    return rtn;
  }

  public <T extends CacheableEntities> T oneById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx) {
    return oneById(cacheType, id, ctx, false);
  }

  public <T extends CacheableEntities> T oneById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx, boolean rtnNullIfNotEqualsOne) {
    try {
      T t = cacheType.newInstance();
      t.setId(id);
      return one(cacheType, t, ctx, rtnNullIfNotEqualsOne, false);
    } catch (InstantiationException | IllegalAccessException e) {
      log.error(OM3.p(cacheType, id, ctx, rtnNullIfNotEqualsOne), e);
      throw new ZeroException(e);
    }
  }

  public <T extends CacheableEntities> T oneEii(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return one(cacheType, t, ctx, false, true);
  }

  public <T extends CacheableEntities> T oneEii(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx, boolean rtnNullIfNotEqualsOne) {
    return one(cacheType, t, ctx, rtnNullIfNotEqualsOne, true);
  }

  public <T extends CacheableEntities> T oneEiiById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx) {
    return oneEiiById(cacheType, id, ctx, false);
  }

  public <T extends CacheableEntities> T oneEiiById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx, boolean rtnNullIfNotEqualsOne) {
    try {
      T t = cacheType.newInstance();
      t.setId(id);
      return oneEii(cacheType, t, ctx, rtnNullIfNotEqualsOne);
    } catch (InstantiationException | IllegalAccessException e) {
      log.error(OM3.p(cacheType, id, ctx, rtnNullIfNotEqualsOne), e);
      throw new ZeroException(e);
    }
  }

  public <T extends CacheableEntities> T oneValid(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return oneValid(cacheType, t, ctx, false);
  }

  public <T extends CacheableEntities> T oneValid(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx, boolean rtnNullIfNotEqualsOne) {
    t.setIvd(String0.N);
    return one(cacheType, t, ctx, rtnNullIfNotEqualsOne, false);
  }

  public <T extends CacheableEntities> T oneValidById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx) {
    return oneValidById(cacheType, id, ctx, false);
  }

  public <T extends CacheableEntities> T oneValidById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx, boolean rtnNullIfNotEqualsOne) {
    try {
      T t = cacheType.newInstance();
      t.setIvd(String0.N);
      t.setId(id);
      return one(cacheType, t, ctx, rtnNullIfNotEqualsOne, false);
    } catch (InstantiationException | IllegalAccessException e) {
      log.error(OM3.p(cacheType, id, ctx, rtnNullIfNotEqualsOne), e);
      throw new ZeroException(e);
    }
  }
}
