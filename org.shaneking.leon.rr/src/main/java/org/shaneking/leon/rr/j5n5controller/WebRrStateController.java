package org.shaneking.leon.rr.j5n5controller;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.zero.crypto.SKC1;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.UUID0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@RequestMapping(path = {"${sk.leon.rr.ctl.state.prefix:/leon/rr/state}"})
@RestController
@Slf4j
public class WebRrStateController {
  public static final String HEALTH_BAD = "@I@AM@BAD@";
  public static final String HEALTH_GOOD = "@I@AM@GOOD@";
  @Value("${sk.leon.rr.file.temporary.folder:/tmp}")
  private String temporaryFolder;
  @Autowired
  private Environment environment;

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/env/{key}"})
  public String stateEnvKey(@PathVariable String key) {
    return environment.getProperty(key);
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/graceful/{sleeps}"})
  public String stateGracefulSleeps(@PathVariable Long sleeps) {
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

  @RequestMapping(path = {"/health"})
  public String stateHealth() {
    return HEALTH_GOOD;
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/skc1/slat"})
  public String stateSkc1slat() {
    return SKC1.salt();
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = {"/uuid0/cul33"})
  public String stateUuid0cul33() {
    return Arrays.stream(String0.repeat(Resp.CODE_UNKNOWN_EXCEPTION, 100).split(String0.MINUS)).map(s -> UUID0.cUl33()).collect(Collectors.joining(String0.BLANK));
  }
}
