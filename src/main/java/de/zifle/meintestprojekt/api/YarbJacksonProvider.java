package de.zifle.meintestprojekt.api;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class YarbJacksonProvider extends ResteasyJackson2Provider {

	public YarbJacksonProvider() {
		ObjectMapper mapper = new ObjectMapper();

		mapper.registerModule(new JavaTimeModule());
		
		setMapper(mapper);
	}
}
