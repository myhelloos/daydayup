package continuous.alfred.study.httpinvoke.feignandribbontimout;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "continuous.alfred.study.httpinvoke.feignandribbontimout")
public class AutoConfig {}
