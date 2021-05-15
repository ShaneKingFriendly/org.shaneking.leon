package org.shaneking.leon.rr.g5n;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.rr.c5c.ht2p.WebRrHt2pExchangePriObjItemBean;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.shaneking.roc.rr.annotation.RrCrypto;
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
  @RrCrypto
  public Resp<Req<List<WebRrHt2pExchangePriObjItemBean>, List<String>>> ht2pExchange(@RequestBody Req<List<WebRrHt2pExchangePriObjItemBean>, List<String>> req) {
    Resp<Req<List<WebRrHt2pExchangePriObjItemBean>, List<String>>> resp = Resp.success(req);
    try {
      req.getPri().setRtn(List0.newArrayList());

      RestTemplate restTemplate = null;
      HttpHeaders httpHeaders = null;
      HttpEntity httpEntity = null;
      for (WebRrHt2pExchangePriObjItemBean ht2pBean : req.getPri().getObj()) {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.putAll(ht2pBean.getHeaders());
        httpEntity = new HttpEntity(ht2pBean.getMapBody() == null ? String0.nullToEmpty(ht2pBean.getStrBody()) : ht2pBean.getMapBody(), httpHeaders);
        log.info(OM3.writeValueAsString(httpEntity));
        ResponseEntity<String> responseEntity = restTemplate.exchange(ht2pBean.getUrl(), HttpMethod.resolve(ht2pBean.getRequestType()), httpEntity, String.class);
        log.info(OM3.writeValueAsString(responseEntity));
        req.getPri().getRtn().add(responseEntity.hasBody() ? responseEntity.getBody() : String0.EMPTY);
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }
}
