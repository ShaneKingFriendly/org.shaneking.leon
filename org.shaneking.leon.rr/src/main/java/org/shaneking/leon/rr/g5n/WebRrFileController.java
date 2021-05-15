package org.shaneking.leon.rr.g5n;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.rr.i5n.WebRrFileRr;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.rr.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(path = {"${sk.leon.rr.ctl.file.prefix:/leon/rr/file}"})
@RestController
@Slf4j
public class WebRrFileController {
  @Autowired
  private WebRrFileRr fileRr;

  @PostMapping(path = {"/attach"})
  public Resp<Req<String, String>> fileAttach(@RequestParam("req") String req, @RequestParam(name = "file") MultipartFile multipartFile) {
    return fileRr.attach(OM3.readValue(req, new TypeReference<Req<String, String>>() {
    }), multipartFile);
  }

  @PostMapping(path = {"/upload"})
  public Resp<Req<String, String>> fileUpload(@RequestParam("req") String req, @RequestParam(name = "file") MultipartFile multipartFile) {
    return fileRr.upload(OM3.readValue(req, new TypeReference<Req<String, String>>() {
    }), multipartFile);
  }
}
