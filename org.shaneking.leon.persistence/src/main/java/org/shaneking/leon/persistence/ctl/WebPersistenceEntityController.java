package org.shaneking.leon.persistence.ctl;

import lombok.Getter;
import org.shaneking.leon.persistence.biz.WebPersistenceEntityBiz;
import org.shaneking.ling.persistence.CacheableEntities;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.rr.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class WebPersistenceEntityController<T extends CacheableEntities> {
  @Getter
  private Class<T> entityClass;
  @Autowired
  @Getter
  private WebPersistenceEntityBiz entityBiz;

  @PostMapping(path = {"/add"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<T>> add(@RequestBody Req<T> req) {
    return this.getEntityBiz().add(req, this.getEntityClass());
  }

  @PostMapping(path = {"/csv"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<String, Req<String>> csv(@RequestBody Req<String> req) {
    return this.getEntityBiz().csv(req, this.getEntityClass());
  }

  @PostMapping(path = {"/del"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<T>> del(@RequestBody Req<T> req) {
    return this.getEntityBiz().del(req, this.getEntityClass());
  }

  @PostMapping(path = {"/ivd"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<T>> ivd(@RequestBody Req<T> req) {
    return this.getEntityBiz().ivd(req, this.getEntityClass());
  }

  @PostMapping(path = {"/lst"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<List<T>, Req<T>> lst(@RequestBody Req<T> req) {
    return this.getEntityBiz().lst(req, this.getEntityClass());
  }

  @PostMapping(path = {"/mge"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<T>> mge(@RequestBody Req<T> req) {
    return this.getEntityBiz().mge(req, this.getEntityClass());
  }

  @PostMapping(path = {"/mod"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<T>> mod(@RequestBody Req<T> req) {
    return this.getEntityBiz().mod(req, this.getEntityClass());
  }

  @PostMapping(path = {"/one"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<T, Req<T>> one(@RequestBody Req<T> req) {
    return this.getEntityBiz().one(req, this.getEntityClass());
  }

  @PostMapping(path = {"/rmv"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<T>> rmv(@RequestBody Req<T> req) {
    return this.getEntityBiz().rmv(req, this.getEntityClass());
  }

  @PostMapping(path = {"/tpl"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<String, Req<String>> tpl(@RequestBody Req<String> req) {
    return this.getEntityBiz().tpl(req, this.getEntityClass());
  }

  @PostMapping(path = {"/xlsx"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<String>> xlsx(@RequestBody Req<String> req) {
    return this.getEntityBiz().xlsx(req, this.getEntityClass());
  }
}
