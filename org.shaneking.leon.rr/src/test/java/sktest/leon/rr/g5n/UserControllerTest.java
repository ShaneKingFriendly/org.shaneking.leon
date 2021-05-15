package sktest.leon.rr.g5n;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;
import org.shaneking.ling.zero.lang.String0;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    assertEquals(String0.ARY_DEC, String0.DIGITAL);

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
