package sktest.leon.test.ctl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
public class HelloController {

  @RequestMapping("/world")
  @ResponseBody
  public UserBean world(@RequestBody UserBean userBean) {
    return userBean;
  }

  @Accessors(chain = true)
  @ToString(callSuper = true)
  public static class UserBean {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
  }
}
