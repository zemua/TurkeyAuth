package devs.mrp.springturkey.services.oauth.factory.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import devs.mrp.springturkey.services.oauth.factory.BaseUrlProvider;

@Component
public class BaseUrlProviderImpl implements BaseUrlProvider {

	@Value("${turkey.authscheme}")
	private String authScheme;

	@Value("${turkey.authport}")
	private int authPort;

	@Value("${turkey.authhost}")
	private String authHost;

	@Override
	public String resolveBaseUrl() {
		return authScheme + "://" + authHost + ":" + authPort;
	}

}
