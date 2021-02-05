package org.shaneking.leon.persistence.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.persistence.biz.FileBiz;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.util.Date0;
import org.shaneking.roc.rr.Req;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileBizImpl implements FileBiz {
  @Value("${sk.leon.biz.file.attach.folder:/tmp}")
  private String attachFolder;
  @Value("${sk.leon.biz.file.upload.folder:/tmp}")
  private String uploadFolder;

  @Override
  public Resp<Req<String, String>> attach(Req<String, String> req, MultipartFile multipartFile) {
    return transform(req, multipartFile, attachFolder);
  }

  @Override
  public Resp<Req<String, String>> upload(Req<String, String> req, MultipartFile multipartFile) {
    return transform(req, multipartFile, uploadFolder);
  }

  private Resp<Req<String, String>> transform(Req<String, String> req, MultipartFile multipartFile, String folder) {
    Resp<Req<String, String>> resp = Resp.success(req);
    Path path = Paths.get(folder, String.valueOf(req.gnnCtx().gnaTenantId()), Date0.on().ySmSd(), req.getPub().gnnTracingId(), new File(String.valueOf(multipartFile.getOriginalFilename())).getName());
    path.toFile().getParentFile().mkdirs();
    try {
      multipartFile.transferTo(path);
      req.getPri().setRtn(path.toFile().getAbsolutePath());
    } catch (Exception e) {
      log.error(OM3.p(req, folder), e);
      resp.parseExp(e);
    }
    return resp;
  }
}
