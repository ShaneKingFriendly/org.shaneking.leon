package sktest.leon.rr.j5n5controller;

import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebRrHt2pControllerTest extends SKSpringMvcUnit {

  @Test
  void ht2pExchange() throws Exception {
    assertNotNull(performJT("/leon/rr/ht2p/exchange"));
  }
}
