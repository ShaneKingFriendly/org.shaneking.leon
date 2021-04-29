package org.shaneking.leon.rr.ac;

import org.shaneking.leon.rr.biz.WebRrFileBiz;
import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.shaneking.roc.rr.annotation.RrCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class WebRrFileAc {
  @Autowired
  private WebRrFileBiz fileBiz;

  @RrAudit
  @RrCrypto
  public Resp<Req<String, String>> attach(Req<String, String> req, MultipartFile multipartFile) {
    return fileBiz.attach(req, multipartFile);
  }

  @RrAudit
  @RrCrypto
  public Resp<Req<String, String>> upload(Req<String, String> req, MultipartFile multipartFile) {
    return fileBiz.upload(req, multipartFile);
  }
}
