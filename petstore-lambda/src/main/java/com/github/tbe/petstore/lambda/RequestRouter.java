package com.github.tbe.petstore.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.github.tbe.petstore.lambda.action.Action;
import com.github.tbe.petstore.lambda.exception.BadRequestException;
import com.github.tbe.petstore.lambda.exception.InternalErrorException;
import com.github.tbe.petstore.lambda.exception.UnauthorizedAccessException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RequestRouter {

	private static final Logger LOGGER = LogManager.getLogger(RequestRouter.class);

	public static void lambdaHandler(InputStream request, OutputStream response, Context context) throws BadRequestException, InternalErrorException, UnauthorizedAccessException {
		try {
			JsonObject inputObject;

			try {
				JsonParser parser = new JsonParser();
				String json = IOUtils.toString(request, StandardCharsets.UTF_8);
				LOGGER.info("Payload: {}", json);
				inputObject = parser.parse(json).getAsJsonObject();
			} catch (NullPointerException | IOException cause) {
				LOGGER.error(String.format("Error while reading request: %1$s", cause.getMessage()), cause);
				throw new InternalErrorException(cause);
			}

			if (Objects.isNull(inputObject)
					|| Objects.isNull(inputObject.get("action"))
					|| StringUtils.isBlank(inputObject.get("action").getAsString())) {
				LOGGER.error("Invald inputObj, could not find action parameter");
				throw new BadRequestException("Could not find action value in request");
			}

			if (Objects.isNull(inputObject)
					|| Objects.isNull(inputObject.get("body"))) {
				LOGGER.error("Invald inputObj, could not find body parameter");
				throw new BadRequestException("Could not find body value in request");
			}

			CognitoAuthenticatedUserProvider authenticatedUserProvider = new CognitoAuthenticatedUserProvider(inputObject);
			AdminGetUserResult user = authenticatedUserProvider.getUser();

			String actionClassName = inputObject.get("action").getAsString();
			Action action;

			try {
				action = Action.class.cast(Class.forName(actionClassName).newInstance());
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException cause) {
				throw new InternalErrorException(cause);
			}

			JsonObject body = inputObject.get("body").getAsJsonObject();
			String output = action.handle(body, context, user);

			try {
				IOUtils.write(output, response, StandardCharsets.UTF_8);
			} catch (NullPointerException | IOException cause) {
				LOGGER.error(String.format("Error while writing response: %1$s", cause.getMessage()), cause);
				throw new InternalErrorException(cause);
			}
			
			
		} catch (Exception cause) {
			if (BadRequestException.class.isAssignableFrom(cause.getClass())
					|| InternalErrorException.class.isAssignableFrom(cause.getClass())
					|| UnauthorizedAccessException.class.isAssignableFrom(cause.getClass())) {
				throw cause;
			}
			
			throw new InternalErrorException(cause.getMessage(), cause);
		}		
	}
}
