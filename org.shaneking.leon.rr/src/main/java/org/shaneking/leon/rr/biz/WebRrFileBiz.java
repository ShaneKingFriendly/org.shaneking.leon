package org.shaneking.leon.rr.biz;

import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.rr.Req;
import org.springframework.web.multipart.MultipartFile;

public interface WebRrFileBiz {
  Resp<Req<String, String>> attach(Req<String, String> req, MultipartFile multipartFile);

  Resp<Req<String, String>> upload(Req<String, String> req, MultipartFile multipartFile);
}
