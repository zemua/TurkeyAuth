package devs.mrp.springturkey.services.oauth;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;

import reactor.core.publisher.Mono;

public interface UserInfoCase {

	public Mono<UserInfo> getUserInfo(String email);

}
