package devs.mrp.springturkey.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Value("${jwk.endpoint}")
	private String jwkEndpoint;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
		.authorizeExchange()
		.pathMatchers("/client/token").permitAll()
		.pathMatchers("/user/token").permitAll()
		.pathMatchers("/user/refresh").permitAll()
		.anyExchange().authenticated()
		.and().csrf().disable();

		http.oauth2ResourceServer().jwt();

		return http.build();
	}

}
