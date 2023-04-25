package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nimbusds.common.contenttype.ContentType;

import devs.mrp.springturkey.services.oauth.RequestForwarder;
import devs.mrp.springturkey.services.oauth.factory.BaseUrlProvider;
import reactor.core.publisher.Mono;

@Service
@Qualifier("token")
public class TokenRequestForwarder implements RequestForwarder<Object,Object> {

	@Value("${turkey.realm}")
	private String realm;

	@Autowired
	private WebClient.Builder clientBuilder;

	@Value("${turkey.authscheme}")
	private String authScheme;

	@Value("${turkey.authport}")
	private int authPort;

	@Value("${turkey.authhost}")
	private String authHost;

	@Autowired
	private BaseUrlProvider baseUrlProvider;

	@Override
	public Mono<ResponseEntity<Object>> forward(Mono<Object> request, Class<Object> outputClass) { // TODO make a decorator for each return type case
		return request.flatMap(req -> sendRequest(request, outputClass));
	}

	private Mono<ResponseEntity<Object>> sendRequest(Object request, Class<Object> outputClass) {
		return buildClient()
				.post()
				.uri("")
				.body(BodyInserters.fromValue(request))
				.exchangeToMono(response -> response.toEntity(outputClass));
	}

	private WebClient buildClient() {
		return clientBuilder
				.baseUrl(baseUrlProvider.resolveBaseUrl())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getType())
				.build();
	}

}
