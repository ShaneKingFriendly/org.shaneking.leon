package org.shaneking.leon.rr.j5r5rr;

import org.shaneking.leon.rr.n5n5service.WebRrFileService;
import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.shaneking.roc.rr.annotation.RrCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class WebRrFileRr {
  @Autowired
  private WebRrFileService fileService;

  @RrAudit
  @RrCrypto
  public Resp<Req<String, String>> attach(Req<String, String> req, MultipartFile multipartFile) {
    return fileService.attach(req, multipartFile);
  }

  @RrAudit
  @RrCrypto
  public Resp<Req<String, String>> upload(Req<String, String> req, MultipartFile multipartFile) {
    return fileService.upload(req, multipartFile);
  }
}
