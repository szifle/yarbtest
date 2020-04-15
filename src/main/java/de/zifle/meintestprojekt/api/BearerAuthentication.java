package de.zifle.meintestprojekt.api;

import java.io.IOException;
import java.util.function.Supplier;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

public class BearerAuthentication implements ClientRequestFilter {

	private final Supplier<String> tokenProvider;

	public BearerAuthentication(Supplier<String> tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		String token = tokenProvider.get();

		if (token != null) {
			requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		}
	}
}