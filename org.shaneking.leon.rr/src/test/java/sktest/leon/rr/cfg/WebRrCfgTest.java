package sktest.leon.rr.cfg;

import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;
import org.shaneking.ling.zero.lang.String0;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebRrCfgTest extends SKSpringMvcUnit {
  @Test
  void test() {
    assertEquals(String0.ARY_DEC, String0.DIGITAL);
  }
}
