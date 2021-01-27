package sktest.leon.test;

import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SKSpringMvcUnitTest extends SKSpringMvcUnit {
  @Test
  void test() throws Exception {
    assertNotNull(performJJ("/hello/world"));
  }
}
