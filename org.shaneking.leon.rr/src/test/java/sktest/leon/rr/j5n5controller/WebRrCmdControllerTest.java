package sktest.leon.rr.j5n5controller;

import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebRrCmdControllerTest extends SKSpringMvcUnit {

  @Test
  void cmdExec() throws Exception {
    assertNotNull(performJT("/leon/rr/cmd/exec"));
  }
}
