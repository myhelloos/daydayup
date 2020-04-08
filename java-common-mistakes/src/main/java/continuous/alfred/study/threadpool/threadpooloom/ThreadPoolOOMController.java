package continuous.alfred.study.threadpool.threadpooloom;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/threadpooloom")
@Slf4j
public class ThreadPoolOOMController {

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

  @GetMapping("oom1")
  public void oom1() throws InterruptedException {
    ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    printStats(threadPool);

    for (int i = 0; i < 100000000; i++) {
      threadPool.execute(
          () -> {
            String payload =
                IntStream.rangeClosed(1, 1000000)
                        .mapToObj(__ -> "a")
                        .collect(Collectors.joining(""))
                    + UUID.randomUUID().toString();

            try {
              TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException ignored) {
            }

            log.info(payload);
          });
    }

    threadPool.shutdown();
    threadPool.awaitTermination(1, TimeUnit.HOURS);
  }

  @GetMapping("oom2")
  public void oom2() throws InterruptedException {
    ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    printStats(threadPool);

    for (int i = 0; i < 100000000; i++) {
      threadPool.execute(
          () -> {
            String payload = UUID.randomUUID().toString();

            try {
              TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException ignored) {
            }

            log.info(payload);
          });
    }

    threadPool.shutdown();
    threadPool.awaitTermination(1, TimeUnit.HOURS);
  }

  @GetMapping("right")
  public int right() throws InterruptedException {
    AtomicInteger atomicInteger = new AtomicInteger();
    ThreadPoolExecutor threadPool =
        new ThreadPoolExecutor(
            2,
            5,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("threadpooloom-demo-threadpool-%d").get(),
            new ThreadPoolExecutor.AbortPolicy());

    threadPool.prestartAllCoreThreads();
    threadPool.allowCoreThreadTimeOut(true);
    printStats(threadPool);

    IntStream.rangeClosed(1, 20)
        .forEach(
            i -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              int id = atomicInteger.incrementAndGet();
              try {
                threadPool.submit(
                    () -> {
                      log.info("{} started", id);

                      try {
                        TimeUnit.SECONDS.sleep(10);
                      } catch (InterruptedException ignored) {
                      }

                      log.info("{} finished", id);
                    });
              } catch (Exception ex) {
                log.error("error submitting task {}", id, ex);
                atomicInteger.decrementAndGet();
              }
            });

    TimeUnit.SECONDS.sleep(60);
    return atomicInteger.intValue();
  }
}
