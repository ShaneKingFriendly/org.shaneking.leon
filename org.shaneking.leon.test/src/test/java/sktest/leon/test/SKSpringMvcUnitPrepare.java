package sktest.leon.test;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class SKSpringMvcUnitPrepare {

  @RequestMapping("/user")
  @ResponseBody
  public UserBean user(@RequestBody UserBean userBean) {
    return userBean;
  }

  @Accessors(chain = true)
  @ToString
  public static class UserBean {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
  }
}
