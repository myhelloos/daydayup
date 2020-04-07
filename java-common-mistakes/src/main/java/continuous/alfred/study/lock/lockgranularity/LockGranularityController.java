package continuous.alfred.study.lock.lockgranularity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lockgranularity")
@Slf4j
public class LockGranularityController {
  private final List<Integer> data = new ArrayList<>();

  private void slow() {
    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (InterruptedException ignored) {
    }
  }

  @GetMapping("/wrong")
  public int wrong() {
    long begin = System.currentTimeMillis();
    IntStream.rangeClosed(1, 1000)
        .parallel()
        .forEach(
            i -> {
              synchronized (this) {
                slow();
                data.add(i);
              }
            });
    log.info("took:{}", System.currentTimeMillis() - begin);
    return data.size();
  }

  @GetMapping("/right")
  public int right() {
    long begin = System.currentTimeMillis();
    IntStream.rangeClosed(1, 1000)
        .parallel()
        .forEach(
            i -> {
              slow();
              synchronized (data) {
                data.add(i);
              }
            });
    log.info("took:{}", System.currentTimeMillis() - begin);
    return data.size();
  }
}
