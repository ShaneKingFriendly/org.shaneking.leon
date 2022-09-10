package org.shaneking.leon.rr.j5r5rr;

import org.shaneking.leon.rr.n5n5service.WebRrFileService;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.rr.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class WebRrFileRr {
  @Autowired
  private WebRrFileService fileService;

  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<String, Req<String>> attach(Req<String> req, MultipartFile multipartFile) {
    return fileService.attach(req, multipartFile);
  }

  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<String, Req<String>> upload(Req<String> req, MultipartFile multipartFile) {
    return fileService.upload(req, multipartFile);
  }
}
