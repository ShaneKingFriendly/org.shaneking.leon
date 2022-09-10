package org.shaneking.leon.rr.j5n5controller;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.rr.bean.ht2p.WebRrHt2pExchangePriObjItemBean;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.roc.rr.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequestMapping(path = {"${sk.leon.rr.ctl.ht2p.prefix:/leon/rr/ht2p}"})
@RestController
@Slf4j
public class WebRrHt2pController {

  @PostMapping(path = {"/exchange"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<List<String>, Req<List<WebRrHt2pExchangePriObjItemBean>>> ht2pExchange(@RequestBody Req<List<WebRrHt2pExchangePriObjItemBean>> req) {
    Resp<List<String>, Req<List<WebRrHt2pExchangePriObjItemBean>>> resp = Resp.success(req, null);
    try {
      resp.srtMsgBodyData(List0.newArrayList());

      RestTemplate restTemplate = null;
      HttpHeaders httpHeaders = null;
      HttpEntity httpEntity = null;
      for (WebRrHt2pExchangePriObjItemBean ht2pBean : req.gnaMsgBdyObj()) {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.putAll(ht2pBean.getHeaders());
        httpEntity = new HttpEntity(ht2pBean.getMapBody() == null ? String0.nullToEmpty(ht2pBean.getStrBody()) : ht2pBean.getMapBody(), httpHeaders);
        log.info(OM3.writeValueAsString(httpEntity));
        ResponseEntity<String> responseEntity = restTemplate.exchange(ht2pBean.getUrl(), HttpMethod.resolve(ht2pBean.getRequestType()), httpEntity, String.class);
        log.info(OM3.writeValueAsString(responseEntity));
        resp.gnaMsgBodyData().add(responseEntity.hasBody() ? responseEntity.getBody() : String0.EMPTY);
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }
}
