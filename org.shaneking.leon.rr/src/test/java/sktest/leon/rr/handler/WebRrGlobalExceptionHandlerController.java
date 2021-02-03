package sktest.leon.rr.handler;

import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.rr.RespException;
import org.shaneking.ling.zero.lang.ZeroException;
import org.shaneking.roc.rr.Req;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/exception")
@RestController
public class WebRrGlobalExceptionHandlerController {

  @RequestMapping("/zero")
  @ResponseBody
  public Resp<Req<String, String>> zero(@RequestBody Req<String, String> req) {
    throw new ZeroException();
  }

  @RequestMapping("/resp")
  @ResponseBody
  public Resp<Req<String, String>> resp(@RequestBody Req<String, String> req) {
    throw new RespException(Resp.failed("", "", req));
  }
}
