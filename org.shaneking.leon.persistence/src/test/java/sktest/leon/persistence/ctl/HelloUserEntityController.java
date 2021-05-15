package sktest.leon.persistence.ctl;

import lombok.Getter;
import org.shaneking.leon.persistence.ctl.WebPersistenceEntityController;
import org.shaneking.roc.persistence.hello.HelloUserEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class HelloUserEntityController extends WebPersistenceEntityController<HelloUserEntity> {
  @Getter
  private final Class<HelloUserEntity> entityClass = HelloUserEntity.class;

}
