package org.shaneking.leon.persistence.ctl;

import lombok.Getter;
import org.shaneking.leon.persistence.biz.WebPersistenceEntityBiz;
import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.persistence.CacheableEntities;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.rr.annotation.RrAccess;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.shaneking.roc.rr.annotation.RrCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class WebPersistenceEntityController<T extends CacheableEntities> {
  @Getter
  private Class<T> entityClass;

  @Autowired
  private WebPersistenceEntityBiz entityBiz;

  @PostMapping(path = {"/mge"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> mge(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().mge(req, this.getEntityClass());
  }

  @PostMapping(path = {"/add"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> add(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().add(req, this.getEntityClass());
  }

  @PostMapping(path = {"/rmv"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> rmv(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().rmv(req, this.getEntityClass());
  }

  @PostMapping(path = {"/del"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> del(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().del(req, this.getEntityClass());
  }

  @PostMapping(path = {"/ivd"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> ivd(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().ivd(req, this.getEntityClass());
  }

  @PostMapping(path = {"/ivdById"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> ivdById(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().ivdById(req, this.getEntityClass());
  }

  @PostMapping(path = {"/mod"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> mod(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().mod(req, this.getEntityClass());
  }

  @PostMapping(path = {"/modById"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, Integer>> modById(@RequestBody Req<T, Integer> req) {
    return this.getEntityBiz().modById(req, this.getEntityClass());
  }

  @PostMapping(path = {"/lst"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, List<T>>> lst(@RequestBody Req<T, List<T>> req) {
    return this.getEntityBiz().lst(req, this.getEntityClass());
  }

  @PostMapping(path = {"/one"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<T, T>> one(@RequestBody Req<T, T> req) {
    return this.getEntityBiz().one(req, this.getEntityClass());
  }

  @PostMapping(path = {"/csv"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<String, String>> csv(@RequestBody Req<String, String> req) {
    return this.getEntityBiz().csv(req, this.getEntityClass());
  }

  @PostMapping(path = {"/template"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<String, String>> template(@RequestBody Req<String, String> req) {
    return this.getEntityBiz().template(req, this.getEntityClass());
  }

  @PostMapping(path = {"/xlsx"})
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<String, Integer>> xlsx(@RequestBody Req<String, Integer> req) {
    return this.getEntityBiz().xlsx(req, this.getEntityClass());
  }

  public WebPersistenceEntityBiz getEntityBiz() {
    return entityBiz;
  }
}
