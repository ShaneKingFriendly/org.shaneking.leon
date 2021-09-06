package sktest.leon.rr.j5n5controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;
import org.shaneking.ling.zero.crypto.SKC1;
import org.shaneking.ling.zero.lang.String0;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserControllerTest extends SKSpringMvcUnit {

  @Test
  void lst() {
    assertEquals(String0.ARY_DEC, String0.DIGITAL);

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    executorService.submit(() -> {
      while (true) {
        try {
          assertNotNull(performJJ("/user/lst"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      //
    }
  }

  @Test
  void curd() throws Exception {
    assertAll(
      () -> assertEquals("E+MK32CVXixTTTOO1qj87l5vpYHfjEsIuIs8usDQV3wiHjrIB6mPFHPr+puaXS4a0ydTyJDDxTFg2x9CXbDzaj1cUts99qHO7e+dEpC3XZt3m4YTMk75u8JyQYFwGr1mvdnu0kxLYIe7nbSbsrcqyQ=="
        , SKC1.encrypt("{\"ext\":{\"tenantNo\":\"tstTenantNo\",\"userNo\":\"tstUserNo\"},\"obj\":\"1612353237501_DcNd45KtJXPmSpz2xRB\",\"rtn\":1}")),
      () -> assertEquals("E+MK32CVXixTTTOO1qj87l5vpYHfjEsIuIs8usDQV3wiHjrIB6mPFHPr+puaXS4aHvKngJ3V2+eDw8AJ9LqXjVhe3bBWpT1wFyDvgQztlXebcNL87fKMOJ42CE9k234lEkfiETuanKawvgTwwJ1TcUo+5QWZ/rMQ1UwKbOfCIqg="
        , SKC1.encrypt("{\"ext\":{\"tenantNo\":\"tstTenantNo\",\"userNo\":\"tstUserNo\"},\"obj\":{\"id\":\"1612353237501_DcNd45KtJXPmSpz2xRB\"},\"rtn\":1}"))
    );

    for (int i = 0; i < 10; i++) {
      setTstSeq("add");
      assertNotNull(performJJ("/user/add"));
      setTstSeq("modByIdVer");
      assertNotNull(performJJ("/user/modByIdVer"));
      setTstSeq("rmvById");
      assertNotNull(performJJ("/user/rmvById"));
    }
  }
}
