package org.shaneking.leon.persistence.biz;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.persistence.dto.WebPersistenceEntityDto;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.util.Date0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.roc.persistence.CacheableEntities;
import org.shaneking.roc.rr.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class WebPersistenceEntityBiz {
  @Value("${sk.leon.persistence.file.temporary.folder:/tmp}")
  private String temporaryFolder;
  @Autowired
  private WebPersistenceEntityDto entityDto;

  public <T extends CacheableEntities> Resp<Req<T, Integer>> mge(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.mge(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> add(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.add(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> rmv(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.rmv(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> rmvById(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.rmvById(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> ivd(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.ivd(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> ivdById(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.ivdById(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> mod(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.mod(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, Integer>> modById(Req<T, Integer> req, Class<T> entityClass) {
    return entityDto.modById(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, List<T>>> lst(Req<T, List<T>> req, Class<T> entityClass) {
    return entityDto.lst(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<T, T>> one(Req<T, T> req, Class<T> entityClass) {
    return entityDto.one(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<String, String>> csv(Req<String, String> req, Class<T> entityClass) {
    return entityDto.csv(req, entityClass);
  }

  public <T extends CacheableEntities> Resp<Req<String, Integer>> xlsx(Req<String, Integer> req, Class<T> entityClass) {
    return entityDto.xlsx(req, entityClass);
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
}
