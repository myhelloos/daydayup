package continuous.alfred.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    //        Utils.loadPropertySource(Application.class, "/tomcat.properties");

    SpringApplication.run(Application.class, args);
  }
}
