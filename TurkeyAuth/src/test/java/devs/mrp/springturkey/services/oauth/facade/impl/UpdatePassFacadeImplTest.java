package devs.mrp.springturkey.services.oauth.facade.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.exceptions.NonExistingTurkeyUserException;
import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.SendEmailCase;
import devs.mrp.springturkey.services.oauth.UserInfoCase;
import devs.mrp.springturkey.services.oauth.dtos.UserInfoDto;
import devs.mrp.springturkey.services.oauth.factory.EmailSenderFactory;
import devs.mrp.springturkey.services.oauth.impl.EmailCommand;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class UpdatePassFacadeImplTest {

	@Mock
	private UserInfoCase userInfoCase;
	@Mock
	private SendEmailCase changePasswordCase;
	@Mock
	private EmailSenderFactory emailSenderFactory;
	@Mock
	private AuthClient authClient;

	@InjectMocks
	private UpdatePassFacadeImpl updatePassFacadeImpl;

	@Captor
	private ArgumentCaptor<Mono<String>> userInfoArgumentCaptor;
	@Captor
	private ArgumentCaptor<Mono<String>> changePasswordArgumentCaptor;

	@BeforeEach
	void setup() {
		when(emailSenderFactory.get(EmailCommand.UPDATE_PASSWORD)).thenReturn(changePasswordCase);
	}

	@Test
	void testSuccess() {
		String email = "test@email.com";
		Mono<String> monoMail = Mono.just(email);
		UserInfoDto userInfoDto = UserInfoDto.builder().id("someid").build();
		Mono<UserInfoDto> monoUserInfo = Mono.just(userInfoDto);
		WebClient client = mock(WebClient.class);

		when(authClient.getClient()).thenReturn(Mono.just(client));
		when(userInfoCase.getUserInfo(ArgumentMatchers.any(), ArgumentMatchers.any(WebClient.class))).thenReturn(monoUserInfo);
		when(changePasswordCase.execute(ArgumentMatchers.any(), ArgumentMatchers.any(WebClient.class))).thenReturn(Mono.just("someid"));

		String result = updatePassFacadeImpl.execute(monoMail).block();

		assertEquals("someid", result);

		verify(userInfoCase, times(1)).getUserInfo(userInfoArgumentCaptor.capture(), ArgumentMatchers.any());
		assertEquals("test@email.com", userInfoArgumentCaptor.getValue().block());

		verify(changePasswordCase, times(1)).execute(changePasswordArgumentCaptor.capture(), ArgumentMatchers.any());
		assertEquals("someid", changePasswordArgumentCaptor.getValue().block());
	}

	@Test
	void testEmptyUser() {
		String email = "test@email.com";
		Mono<String> monoMail = Mono.just(email);
		UserInfoDto userInfoDto = UserInfoDto.builder().id("someid").build();
		Mono<UserInfoDto> monoUserInfo = Mono.just(userInfoDto);
		WebClient client = mock(WebClient.class);

		when(authClient.getClient()).thenReturn(Mono.just(client));
		when(userInfoCase.getUserInfo(ArgumentMatchers.any(), ArgumentMatchers.any(WebClient.class))).thenReturn(Mono.empty());
		when(changePasswordCase.execute(ArgumentMatchers.any(), ArgumentMatchers.any(WebClient.class))).thenReturn(Mono.just("someid"));

		Mono<String> monoResult = updatePassFacadeImpl.execute(monoMail);
		StepVerifier.create(monoResult)
		.expectError(NonExistingTurkeyUserException.class)
		.verify();
	}

}
