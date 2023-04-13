package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.services.oauth.AuthClient;

@Service
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

	@Override
	public WebClient getClient() {
		return clientBuilder
				.baseUrl(authScheme + "://" + authHost + ":" + authPort)
				.defaultHeader("Content-Type", contentType)
				.build();
	}

}
