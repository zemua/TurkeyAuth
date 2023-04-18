package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.exceptions.GetUserInfoException;
import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.UserInfoCase;
import devs.mrp.springturkey.services.oauth.dtos.UserInfoDto;
import reactor.core.publisher.Mono;

@Service
public class UserInfoCaseImpl implements UserInfoCase {

	@Autowired
	private AuthClient authClient;

	@Value("${turkey.realm}")
	private String realm;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<UserInfoDto> getUserInfo(Mono<String> email) {
		return email.flatMap(this::execute);
	}

	private Mono<UserInfoDto> execute(String mail) {
		return authClient.getClient()
				.flatMap(client -> sendRequest(client, "/auth/admin/realms/" + realm + "/users?email=" + mail));
	}

	private Mono<UserInfoDto> sendRequest(WebClient client, String uri) {
		return client.get()
				.uri(uri)
				.<UserInfoDto>exchangeToMono(response -> handleResponse(response));
	}

	private Mono<UserInfoDto> handleResponse(ClientResponse response) {
		if (response.statusCode().is2xxSuccessful()) {
			return response.bodyToMono(UserInfoDto.class);
		} else {
			return response.createException()
					.flatMap(ex -> Mono.error(new GetUserInfoException(ex)));
		}
	}

}
