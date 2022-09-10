package org.shaneking.leon.persistence.dao;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.persistence.CacheableEntities;
import org.shaneking.ling.rr.Ctx;
import org.shaneking.ling.zero.lang.ZeroException;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class ProtectDao {
  @Autowired
  @Getter
  private CacheableDao cacheableDao;

  public <T extends CacheableEntities> int add(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.add(cacheType, CacheableDao.pti(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> long cnt(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.cnt(cacheType, CacheableDao.pts(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> List<T> lst(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.lst(cacheType, CacheableDao.pts(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> List<T> lstByIds(@NonNull Class<T> cacheType, @NonNull List<String> ids, @NonNull Ctx ctx) {
    try {
      return cacheableDao.lstByIds(cacheType, CacheableDao.pts(cacheType.newInstance(), ctx.gnaTenantId()), ids);
    } catch (Exception e) {
      throw new ZeroException(OM3.p(cacheType, ids), e);
    }
  }

  public <T extends CacheableEntities> List<String> lstIds(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.lstIds(cacheType, CacheableDao.pts(t, ctx.gnaTenantId()));
  }

  ///!BeCareful: if no whereConditions, will update all
  public <T extends CacheableEntities> int mod(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.mod(cacheType, CacheableDao.ptu(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> int modByIdsVer(@NonNull Class<T> cacheType, @NonNull T t, @NonNull List<String> ids, @NonNull Ctx ctx) {
    return cacheableDao.modByIdsVer(cacheType, CacheableDao.ptu(t, ctx.gnaTenantId()), ids);
  }

  public <T extends CacheableEntities> int modByIdVer(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.modByIdVer(cacheType, CacheableDao.ptu(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> T one(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.one(cacheType, CacheableDao.pts(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> T one(@NonNull Class<T> cacheType, @NonNull T t, boolean rtnNullIfNotEqualsOne, @NonNull Ctx ctx) {
    return cacheableDao.one(cacheType, CacheableDao.pts(t, ctx.gnaTenantId()), rtnNullIfNotEqualsOne);
  }

  public <T extends CacheableEntities> T oneById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx) {
    try {
      return oneById(cacheType, cacheType.newInstance(), id, ctx);
    } catch (Exception e) {
      throw new ZeroException(OM3.p(cacheType, id), e);
    }
  }

  public <T extends CacheableEntities> T oneById(@NonNull Class<T> cacheType, @NonNull String id, boolean rtnNullIfNotEqualsOne, @NonNull Ctx ctx) {
    try {
      return oneById(cacheType, cacheType.newInstance(), id, rtnNullIfNotEqualsOne, ctx);
    } catch (Exception e) {
      throw new ZeroException(OM3.p(cacheType, id), e);
    }
  }

  public <T extends CacheableEntities> T oneById(@NonNull Class<T> cacheType, @NonNull T t, @NonNull String id, @NonNull Ctx ctx) {
    return cacheableDao.oneById(cacheType, CacheableDao.pts(t, ctx.gnaTenantId()), id);
  }

  public <T extends CacheableEntities> T oneById(@NonNull Class<T> cacheType, @NonNull T t, @NonNull String id, boolean rtnNullIfNotEqualsOne, @NonNull Ctx ctx) {
    return cacheableDao.oneById(cacheType, CacheableDao.pts(t, ctx.gnaTenantId()), id, rtnNullIfNotEqualsOne);
  }

  public <T extends CacheableEntities> int rmv(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.rmv(cacheType, CacheableDao.ptu(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> int rmvById(@NonNull Class<T> cacheType, @NonNull String id, @NonNull Ctx ctx) {
    try {
      return rmvById(cacheType, cacheType.newInstance(), id, ctx);
    } catch (Exception e) {
      throw new ZeroException(OM3.p(cacheType, id), e);
    }
  }

  public <T extends CacheableEntities> int rmvById(@NonNull Class<T> cacheType, @NonNull T t, @NonNull Ctx ctx) {
    return cacheableDao.rmvById(cacheType, CacheableDao.ptu(t, ctx.gnaTenantId()));
  }

  public <T extends CacheableEntities> int rmvById(@NonNull Class<T> cacheType, @NonNull T t, @NonNull String id, @NonNull Ctx ctx) {
    return cacheableDao.rmvById(cacheType, CacheableDao.ptu(t, ctx.gnaTenantId()), id);
  }

  public <T extends CacheableEntities> int rmvByIds(@NonNull Class<T> cacheType, @NonNull List<String> ids, @NonNull Ctx ctx) {
    try {
      return rmvByIds(cacheType, cacheType.newInstance(), ids, ctx);
    } catch (Exception e) {
      throw new ZeroException(OM3.p(cacheType, ids), e);
    }
  }

  public <T extends CacheableEntities> int rmvByIds(@NonNull Class<T> cacheType, @NonNull T t, @NonNull List<String> ids, @NonNull Ctx ctx) {
    return cacheableDao.rmvByIds(cacheType, CacheableDao.ptu(t, ctx.gnaTenantId()), ids);
  }
}
