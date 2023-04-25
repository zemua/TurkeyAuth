package devs.mrp.springturkey.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = {"devs.mrp.springturkey"})
@PropertySource("classpath:application.yml")
public class TurkeyTestConfig {

}
