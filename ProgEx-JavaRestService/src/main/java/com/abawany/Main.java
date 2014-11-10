package com.abawany;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Main extends ResourceConfig {
	public Main() {
		register(JacksonFeature.class);
	}

}
