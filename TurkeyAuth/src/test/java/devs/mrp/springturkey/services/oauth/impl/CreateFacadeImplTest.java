package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import devs.mrp.springturkey.entities.User;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import devs.mrp.springturkey.services.oauth.UserInfoCase;
import devs.mrp.springturkey.services.oauth.dtos.UserInfoDto;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
class CreateFacadeImplTest {

	@Mock
	private CreateUserCase createUserCase;
	@Mock
	private UserInfoCase userInfoCase;

	@InjectMocks
	private CreateFacadeImpl createFacadeImpl;

	@Captor
	private ArgumentCaptor<Mono<String>> stringCaptor;

	@Test
	void test() {
		User user = User.builder().email("some@email.com").secret("pass").build();
		UserInfoDto userInfoDto = UserInfoDto.builder().id("someid").build();
		Mono<User> monoUser = Mono.just(user);
		Mono<String> monoMail = Mono.just("some@email.com");
		Mono<UserInfoDto> monoUserInfo = Mono.just(userInfoDto);

		when(createUserCase.createUser(monoUser)).thenReturn(monoUser);
		when(userInfoCase.getUserInfo(ArgumentMatchers.any())).thenReturn(monoUserInfo);

		User result = createFacadeImpl.execute(monoUser).block();

		assertEquals(user, result);

		verify(createUserCase, times(1)).createUser(Mockito.eq(monoUser));

		verify(userInfoCase, times(1)).getUserInfo(stringCaptor.capture());
		assertEquals("some@email.com", stringCaptor.getValue().block());
	}

}