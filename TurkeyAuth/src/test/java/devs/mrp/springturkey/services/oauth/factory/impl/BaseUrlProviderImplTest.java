package devs.mrp.springturkey.services.oauth.factory.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = { BaseUrlProviderImpl.class })
class BaseUrlProviderImplTest {

	@Autowired
	private BaseUrlProviderImpl resolver;

	@Test
	void test() {
		String result = resolver.resolveBaseUrl();
		assertEquals("http://localhost:28080", result);
	}

}
