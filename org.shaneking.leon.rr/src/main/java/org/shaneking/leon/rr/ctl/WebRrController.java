package org.shaneking.leon.rr.ctl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.leon.rr.bean.WebRrHt2pBean;
import org.shaneking.leon.rr.biz.WebRrFileBiz;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.crypto.SKC1;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.Runtime0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.rr.Req;
import org.shaneking.roc.rr.annotation.RrAudit;
import org.shaneking.roc.rr.annotation.RrCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(path = {"${sk.leon.rr.ctl.prefix:/}"})
@RestController
@Slf4j
public class WebRrController {
  @Value("${sk.leon.rr.file.temporary.folder:/tmp}")
  private String temporaryFolder;
  @Autowired
  private Environment environment;
  @Autowired
  private WebRrFileBiz fileBiz;

  @PostMapping(path = {"/command/exec"})
  @RrAudit
  @RrCrypto
  public Resp<Req<List<String>, List<List<String>>>> commandExec(@RequestBody Req<List<String>, List<List<String>>> req) {
    Resp<Req<List<String>, List<List<String>>>> resp = Resp.success(req);
    try {
      req.getPri().setRtn(List0.newArrayList());

      for (String command : req.getPri().getObj()) {
        req.getPri().getRtn().add(Runtime0.exec(command));
      }
    } catch (Exception e) {
      log.error(OM3.lp(resp, req), e);
      resp.parseExp(e);
    }
    return resp;
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/crypto0/slat"})
  public String crypto0slat() {
    return SKC1.salt();
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/env/{key}"})
  public String envKey(@PathVariable String key) {
    return environment.getProperty(key);
  }

  @PostMapping(path = {"/file/attach"})
  public Resp<Req<String, String>> fileAttach(@RequestParam("req") String req, @RequestParam(name = "file") MultipartFile multipartFile) {
    return fileBiz.attach(OM3.readValue(req, new TypeReference<Req<String, String>>() {
    }), multipartFile);
  }

  @PostMapping(path = {"/file/upload"})
  public Resp<Req<String, String>> fileUpload(@RequestParam("req") String req, @RequestParam(name = "file") MultipartFile multipartFile) {
    return fileBiz.upload(OM3.readValue(req, new TypeReference<Req<String, String>>() {
    }), multipartFile);
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/graceful/{sleeps}"})
  public String gracefulSleeps(@PathVariable Long sleeps) {
    long l = sleeps == null ? 30 * 1000 : sleeps.longValue();
    try {
      Thread.sleep(l);
    } catch (Exception e) {
      log.error(String.valueOf(sleeps), e);
    }
    Path path = Paths.get(temporaryFolder, this.getClass().getName(), "graceful", UUID0.cUl33() + File0.suffix(File0.TYPE_LOG));
    try {
      path.toFile().getParentFile().mkdirs();
      Files.write(path, String.valueOf(l).getBytes());
    } catch (Exception e) {
      log.error(OM3.lp(path, sleeps), e);
    }
    return String.join(String0.COLON, String.valueOf(l), path.toFile().getAbsolutePath());
  }

  @RequestMapping(path = {"/hello/world"})
  public String helloWorld() {
    return "Welcome to ShaneKing!";
  }

  @PostMapping(path = {"/ht2p/exchange"})
  @RrAudit
  @RrCrypto
  public Resp<Req<List<WebRrHt2pBean>, List<String>>> ht2pExchange(@RequestBody Req<List<WebRrHt2pBean>, List<String>> req) {
    Resp<Req<List<WebRrHt2pBean>, List<String>>> resp = Resp.success(req);
    try {
      req.getPri().setRtn(List0.newArrayList());

      RestTemplate restTemplate = null;
      HttpHeaders httpHeaders = null;
      HttpEntity httpEntity = null;
      for (WebRrHt2pBean ht2pBean : req.getPri().getObj()) {
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

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/uuid0/cul33"})
  public String uuid0cul33() {
    return String.join(String0.COMMA, Arrays.stream(String0.repeat(String0.COMMA, 100).split(String0.COMMA)).map(s -> UUID0.cUl33()).collect(Collectors.toList()));
  }
}
