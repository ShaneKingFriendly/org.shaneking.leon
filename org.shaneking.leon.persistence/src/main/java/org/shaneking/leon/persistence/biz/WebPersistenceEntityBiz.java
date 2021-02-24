package org.shaneking.leon.persistence.biz;

import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.persistence.CacheableEntities;
import org.shaneking.roc.rr.Req;

import java.util.List;

public interface WebPersistenceEntityBiz {
  <T extends CacheableEntities> Resp<Req<T, Integer>> mge(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<T, Integer>> add(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<T, Integer>> rmv(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<T, Integer>> del(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<String, Integer>> delById(Req<String, Integer> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<T, Integer>> mod(Req<T, Integer> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<T, List<T>>> lst(Req<T, List<T>> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<T, T>> one(Req<T, T> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<String, String>> csv(Req<String, String> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<String, String>> template(Req<String, String> req, Class<T> entityClass);

  <T extends CacheableEntities> Resp<Req<String, Integer>> xlsx(Req<String, Integer> req, Class<T> entityClass);
}
