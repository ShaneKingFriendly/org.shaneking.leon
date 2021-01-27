package org.shaneking.leon.swagger.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = SwaggerProps.PREFIX, ignoreInvalidFields = true)
public class SwaggerProps {
  public static final String PREFIX = "sk.leon.swagger.ui";
  @Getter
  @Setter
  private boolean enabled = true;
  @Getter
  @Setter
  private String basePkg = "org.shaneking";
  @Getter
  @Setter
  private String pathReg = "/api/\\w*/open/\\S*";
  @Getter
  @Setter
  private String title = "ShaneKing";
  @Getter
  @Setter
  private String version = "1.0";
}
