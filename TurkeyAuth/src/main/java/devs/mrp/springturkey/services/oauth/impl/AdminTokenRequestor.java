package devs.mrp.springturkey.services.oauth.impl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.exceptions.TokenRetrievalException;
import devs.mrp.springturkey.services.oauth.TokenRequestor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AdminTokenRequestor implements TokenRequestor {

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

	@Value("${turkey.realm}")
	private String realm;

	@Value("${turkey.authclient}")
	private String client;

	@Value("${turkey.authsecret}")
	private String secret;

	@Value("${turkey.tokenproperty}")
	private String tokenProperty;

	@Override
	public Mono<String> getToken() {
		return postQuery()
				.flatMap(hashMap -> tokenFromMap(hashMap));
	}

	private Mono<String> tokenFromMap(HashMap<String,String> hashMap) {
		return Optional.ofNullable(hashMap.get(tokenProperty))
				.map(token -> Mono.just(token))
				.orElse(Mono.error(new TokenRetrievalException("Token missing in origin's response")));
	}

	private Mono<HashMap<String,String>> postQuery() {
		return buildWebClient().post()
				.uri(resolveUrl())
				.body(BodyInserters.fromFormData(buildFormData()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<HashMap<String,String>>(){})
				.onErrorMap(e -> new TokenRetrievalException("Error retrieving authorization token", e));
	}

	private WebClient buildWebClient() {
		return clientBuilder
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.build();
	}

	private String resolveUrl() {
		return authScheme + "://" + authHost + ":" + authPort + "/auth/realms/" + realm + "/protocol/openid-connect/token";
	}

	private MultiValueMap<String, String> buildFormData() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("client_id", client);
		formData.add("client_secret", secret);
		formData.add("grant_type", "client_credentials");
		return formData;
	}

}
