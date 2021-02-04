package org.shaneking.leon.biz;

import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.rr.Req;
import org.springframework.web.multipart.MultipartFile;

public interface FileBiz {
  Resp<Req<String, String>> attach(Req<String, String> req, MultipartFile multipartFile);

  Resp<Req<String, String>> upload(Req<String, String> req, MultipartFile multipartFile);
}
