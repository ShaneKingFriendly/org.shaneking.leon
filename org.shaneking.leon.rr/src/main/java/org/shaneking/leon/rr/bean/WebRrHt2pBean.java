package org.shaneking.leon.rr.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@ToString
public class WebRrHt2pBean {
  @Getter
  @Setter
  private String url;
  @Getter
  @Setter
  private String requestType;
  @Getter
  @Setter
  private Map<String, List<String>> headers;
  @Getter
  @Setter
  private LinkedMultiValueMap<String, String> mapBody;
  @Getter
  @Setter
  private String strBody;
}
