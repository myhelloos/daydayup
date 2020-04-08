package continuous.alfred.study.threadpool.threadpoolmixuse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("threadpoolmixuse")
@Slf4j
public class ThreadPoolMixuseController {

  private static ThreadPoolExecutor threadPool =
      new ThreadPoolExecutor(
          2,
          2,
          1,
          TimeUnit.HOURS,
          new ArrayBlockingQueue<>(100),
          new ThreadFactoryBuilder().setNameFormat("batchfileprocess-threadpool-%d").get(),
          new ThreadPoolExecutor.CallerRunsPolicy());

  private static ThreadPoolExecutor asyncCalcThreadPool =
      new ThreadPoolExecutor(
          200,
          200,
          1,
          TimeUnit.HOURS,
          new ArrayBlockingQueue<>(1000),
          new ThreadFactoryBuilder().setNameFormat("asynccalc-threadpool-%d").get());

  private void printStats(ThreadPoolExecutor threadPool) {
    Executors.newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(
            () -> {
              log.info("=========================");
              log.info("Pool Size: {}", threadPool.getPoolSize());
              log.info("Active Threads: {}", threadPool.getActiveCount());
              log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
              log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());

              log.info("=========================");
            },
            0,
            1,
            TimeUnit.SECONDS);
  }

  private Callable<Integer> calcTask() {
    return () -> {
      TimeUnit.MILLISECONDS.sleep(10);
      return 1;
    };
  }

  @GetMapping("wrong")
  public int wrong() throws ExecutionException, InterruptedException {
    return threadPool.submit(calcTask()).get();
  }

  @GetMapping("right")
  public int right() throws ExecutionException, InterruptedException {
    return asyncCalcThreadPool.submit(calcTask()).get();
  }

  //  @PostConstruct
  public void init() {
    printStats(threadPool);

    new Thread(
            () -> {
              String payload =
                  IntStream.rangeClosed(1, 1_000_000)
                      .mapToObj(__ -> "a")
                      .collect(Collectors.joining(""));

              while (true) {
                threadPool.execute(
                    () -> {
                      try {
                        Files.write(
                            Paths.get("demo.txt"),
                            Collections.singletonList(LocalTime.now().toString() + ":" + payload),
                            StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING);
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                      log.info("batch file processing done");
                    });
              }
            })
        .start();
  }
}
