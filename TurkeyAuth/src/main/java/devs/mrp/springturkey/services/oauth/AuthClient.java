package devs.mrp.springturkey.services.oauth;

import org.springframework.web.reactive.function.client.WebClient;

public interface AuthClient {

	public WebClient getClient();

}
