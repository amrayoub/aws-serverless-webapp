package com.github.tbe.petstore.lambda;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.github.tbe.petstore.lambda.exception.BadRequestException;
import com.github.tbe.petstore.lambda.exception.InternalErrorException;
import com.github.tbe.petstore.lambda.exception.UnauthorizedAccessException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEObject;
import com.nimbusds.jose.Payload;

public class CognitoAuthenticatedUserProvider {

	private static final Logger LOGGER = LogManager.getLogger(CognitoAuthenticatedUserProvider.class);

	private final JsonObject inputObject;

	public CognitoAuthenticatedUserProvider(JsonObject inputObject) {
		this.inputObject = inputObject;
	}

	public AdminGetUserResult getUser() throws BadRequestException, InternalErrorException, UnauthorizedAccessException {
		if (Objects.isNull(inputObject.get("headers"))) {
			LOGGER.error("Invald inputObj, could not find headers parameter");
			throw new BadRequestException("Could not find headers value in request");
		}

		JsonObject headers = inputObject.get("headers").getAsJsonObject();

		if (Objects.isNull(headers.get("Authorization"))
				|| StringUtils.isBlank(headers.get("Authorization").getAsString())) {
			LOGGER.error("Unauthorized request, could not find Authorization header");
			throw new UnauthorizedAccessException("Could not find Authorization header");
		}

		String identityToken = headers.get("Authorization").getAsString();
		JOSEObject joseObject;

		try {
			joseObject = JOSEObject.parse(identityToken);
		} catch (Exception cause) {
			LOGGER.error(String.format("Error while reading parsing identity token: %1$s", cause.getMessage()), cause);
			throw new InternalErrorException(cause);
		}

		Payload payload = joseObject.getPayload();

		if (Objects.isNull(payload)) {
			LOGGER.error("Unable to find payload from identity token");
			throw new InternalErrorException("Unable to find payload from identity token");
		}

		JsonObject payloadObject;

		try {
			String json = payload.toString();
			JsonParser parser = new JsonParser();
			payloadObject = parser.parse(json).getAsJsonObject();
		} catch (Exception cause) {
			LOGGER.error(String.format("Error while reading parsing payload json: %1$s", cause.getMessage()), cause);
			throw new InternalErrorException(cause);
		}

		if (Objects.isNull(payloadObject)) {
			LOGGER.error("Unable to find payload from identity token");
			throw new InternalErrorException("Unable to find payload from identity token");
		}

		if (Objects.isNull(payloadObject.get("cognito:username"))
				|| StringUtils.isBlank(payloadObject.get("cognito:username").getAsString())) {
			LOGGER.error("Unauthorized request, could not find cognito username from the payload");
			throw new UnauthorizedAccessException("Could not find cognito username from the payload");
		}

		String cognitoUsername = payloadObject.get("cognito:username").getAsString();

		Properties properties = new Properties();

		try (InputStream stream = CognitoAuthenticatedUserProvider.class.getClassLoader().getResourceAsStream("cognito.properties")) {
			properties.load(stream);
		} catch (Exception cause) {
			LOGGER.error("Unable to load cognito.properties");
			throw new InternalErrorException("Unable to load cognito.properties");
		}

		String regionName = properties.getProperty("cognito.regionName");
		String userPoolId = properties.getProperty("cognito.userPoolId");

		AWSCognitoIdentityProvider identityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
				.withRegion(Regions.fromName(regionName))
				.build();

		AdminGetUserResult user;

		try {
			user = identityProvider.adminGetUser(new AdminGetUserRequest()
					.withUsername(cognitoUsername)
					.withUserPoolId(userPoolId));
		} catch (Exception cause) {
			if (UserNotFoundException.class.isAssignableFrom(cause.getClass())) {
				LOGGER.error("Unauthorized request, could not find user");
				throw new UnauthorizedAccessException("Could not find user");
			}

			LOGGER.error(cause.getMessage(), cause);
			throw new InternalErrorException(cause.getMessage(), cause);
		}

		return user;
	}
}
