package sktest.leon.rr.j5n5controller;

import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebRrFileControllerTest extends SKSpringMvcUnit {

  @Test
  void fileUpload() throws Exception {
    assertNotNull(performJFT("/leon/rr/file/upload", tstITxtFiles()));
  }
}
