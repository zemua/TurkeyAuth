package devs.mrp.springturkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class TurkeyAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurkeyAuthApplication.class, args);
	}

}
