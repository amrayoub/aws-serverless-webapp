package com.github.tbe.petstore.lambda.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractAction implements Action {

	protected Gson getGson() {
		return new GsonBuilder()
				.setPrettyPrinting()
				.create();
	}
}