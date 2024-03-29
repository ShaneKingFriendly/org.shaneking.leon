package org.shaneking.leon.rr.j5n5controller;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.lang.Runtime0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.roc.rr.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(path = {"${sk.leon.rr.ctl.cmd.prefix:/leon/rr/cmd}"})
@RestController
@Slf4j
public class WebRrCmdController {

  @PostMapping(path = {"/exec"})
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<List<List<String>>, Req<List<String>>> cmdExec(@RequestBody Req<List<String>> req) {
    Resp<List<List<String>>, Req<List<String>>> resp = Resp.success(req, null);
    try {
      resp.srtMsgBodyData(List0.newArrayList());

      for (String command : req.gnaMsgBdyObj()) {
        resp.gnaMsgBodyData().add(Runtime0.exec(command));
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }
}
