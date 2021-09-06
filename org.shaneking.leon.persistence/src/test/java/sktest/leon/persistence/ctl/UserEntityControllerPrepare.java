package sktest.leon.persistence.ctl;

import lombok.Getter;
import org.shaneking.leon.persistence.ctl.WebPersistenceEntityController;
import org.shaneking.roc.persistence.simple.SimpleUserEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserEntityControllerPrepare extends WebPersistenceEntityController<SimpleUserEntity> {
  @Getter
  private final Class<SimpleUserEntity> entityClass = SimpleUserEntity.class;

}
