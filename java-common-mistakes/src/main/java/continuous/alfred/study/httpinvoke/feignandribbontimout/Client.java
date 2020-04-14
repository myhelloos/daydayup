package continuous.alfred.study.httpinvoke.feignandribbontimout;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "clientsdk")
public interface Client {
  @GetMapping("/feignandribbon/server")
  void server();
}
