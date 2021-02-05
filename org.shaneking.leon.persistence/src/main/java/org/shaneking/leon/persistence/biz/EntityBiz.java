package org.shaneking.leon.persistence.biz;

import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.persistence.entity.CacheableEntity;
import org.shaneking.roc.rr.Req;

import java.util.List;

public interface EntityBiz {
  <T extends CacheableEntity> Resp<Req<T, Integer>> mge(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<T, Integer>> add(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<T, Integer>> rmv(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<T, Integer>> del(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<T, Integer>> mod(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<T, List<T>>> lst(Req<T, List<T>> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<T, T>> one(Req<T, T> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<String, String>> csvAdd(Req<String, String> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<String, String>> csvMod(Req<String, String> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<String, String>> xlsxTmp(Req<String, String> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<String, Integer>> xlsxImp(Req<String, Integer> req, Class<T> entityClass);

  <T extends CacheableEntity> Resp<Req<String, String>> xlsxExp(Req<String, String> req, Class<T> entityClass);
}
