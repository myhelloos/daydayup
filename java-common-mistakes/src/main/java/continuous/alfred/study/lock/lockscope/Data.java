package continuous.alfred.study.lock.lockscope;

import lombok.Getter;

public class Data {
  @Getter private static int counter = 0;
  private static final Object locker = new Object();

  public static int reset() {
    counter = 0;
    return counter;
  }

  public synchronized void wrong() {
    counter++;
  }

  public void right() {
    synchronized (locker) {
      counter++;
    }
  }
}
