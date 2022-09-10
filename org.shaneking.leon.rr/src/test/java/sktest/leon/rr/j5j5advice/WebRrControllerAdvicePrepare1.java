package sktest.leon.rr.j5j5advice;

import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.ling.rr.RespException;
import org.shaneking.ling.zero.lang.ZeroException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/exception")
@RestController
public class WebRrControllerAdvicePrepare1 {

  @RequestMapping("/resp")
  @ResponseBody
  public Resp<String, Req<String>> resp(@RequestBody Req<String> req) {
    throw new RespException(Resp.failed(req, "", ""));
  }

  @RequestMapping("/zero")
  @ResponseBody
  public Resp<String, Req<String>> zero(@RequestBody Req<String> req) {
    throw new ZeroException();
  }
}
