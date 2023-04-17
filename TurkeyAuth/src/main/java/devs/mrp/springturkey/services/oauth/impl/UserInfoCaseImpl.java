package devs.mrp.springturkey.services.oauth.impl;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;

import devs.mrp.springturkey.services.oauth.UserInfoCase;
import reactor.core.publisher.Mono;

public class UserInfoCaseImpl implements UserInfoCase {

	@Override
	public Mono<UserInfo> getUserInfo(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
