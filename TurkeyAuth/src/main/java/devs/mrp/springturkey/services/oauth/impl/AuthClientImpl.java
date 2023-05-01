package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.TokenRequestor;
import devs.mrp.springturkey.services.oauth.factory.BaseUrlProvider;
import reactor.core.publisher.Mono;

@Component
public class AuthClientImpl implements AuthClient {

	@Autowired
	private WebClient.Builder clientBuilder;

	@Value("${turkey.authscheme}")
	private String authScheme;

	@Value("${turkey.authport}")
	private int authPort;

	@Value("${turkey.authhost}")
	private String authHost;

	@Value("${turkey.authcontenttype}")
	private String contentType;

	@Autowired
	private TokenRequestor adminTokenRequestor;

	@Autowired
	private BaseUrlProvider baseUrlProvider;

	@Override
	public Mono<WebClient> getClient() {
		return adminTokenRequestor.getToken()
				.map(token -> buildAuthorizedClient(token));
	}

	private WebClient buildAuthorizedClient(String token) {
		return clientBuilder
				.baseUrl(baseUrlProvider.resolveBaseUrl())
				.defaultHeaders(httpHeaders -> {
					httpHeaders.set(HttpHeaders.CONTENT_TYPE, contentType);
					httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
				})
				.build();
	}

}
