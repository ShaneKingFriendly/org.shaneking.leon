package org.shaneking.leon.rr.n5n5service;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.time.LD0;
import org.shaneking.ling.zero.util.UUID0;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class WebRrFileService {
  @Value("${sk.leon.rr.file.attach.folder:/tmp}")
  private String attachFolder;
  @Value("${sk.leon.rr.file.upload.folder:/tmp}")
  private String uploadFolder;

  public Resp<String, Req<String>> attach(Req<String> req, MultipartFile multipartFile) {
    return transform(req, multipartFile, attachFolder);
  }

  private Resp<String, Req<String>> transform(Req<String> req, MultipartFile multipartFile, String folder) {
    Resp<String, Req<String>> resp = Resp.success(req, null);
    Path path = Paths.get(folder, String.valueOf(req.gnnCtx().gnaTenantId()), LD0.on().ySmSd(), req.gnnMsg().gnnRno(), UUID0.cUl33(), new File(String.valueOf(multipartFile.getOriginalFilename())).getName());
    path.toFile().getParentFile().mkdirs();
    try {
      multipartFile.transferTo(path);
      resp.srtMsgBodyData(path.toFile().getAbsolutePath());
    } catch (Exception e) {
      log.error(OM3.lp(resp, req, folder), e);
      resp.parseExp(e);
    }
    return resp;
  }

  public Resp<String, Req<String>> upload(Req<String> req, MultipartFile multipartFile) {
    return transform(req, multipartFile, uploadFolder);
  }
}
