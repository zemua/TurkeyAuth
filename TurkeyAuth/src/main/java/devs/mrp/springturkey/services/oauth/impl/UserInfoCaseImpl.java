package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.exceptions.GetUserInfoException;
import devs.mrp.springturkey.services.oauth.UserInfoCase;
import devs.mrp.springturkey.services.oauth.dtos.UserInfoDto;
import reactor.core.publisher.Mono;

@Service
public class UserInfoCaseImpl implements UserInfoCase {

	@Value("${turkey.realm}")
	private String realm;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<UserInfoDto> getUserInfo(Mono<String> email, WebClient webClient) {
		return email.flatMap(mail -> sendRequest(webClient, "/auth/admin/realms/" + realm + "/users?email=" + mail));
	}

	private Mono<UserInfoDto> sendRequest(WebClient client, String uri) {
		return client.get()
				.uri(uri)
				.<UserInfoDto>exchangeToMono(response -> handleResponse(response));
	}

	private Mono<UserInfoDto> handleResponse(ClientResponse response) {
		if (response.statusCode().is2xxSuccessful()) {
			return response.bodyToFlux(UserInfoDto.class).next(); // because we are retrieving info for only a single email that cannot be repeated
		} else {
			return response.createException()
					.flatMap(ex -> Mono.error(new GetUserInfoException(ex)));
		}
	}

}
