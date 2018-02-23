package com.github.tbe.petstore.lambda.action;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.github.tbe.petstore.lambda.action.model.CreatePetRequest;
import com.github.tbe.petstore.lambda.ddb.PetstoreRepository;
import com.github.tbe.petstore.lambda.ddb.model.Pet;
import com.github.tbe.petstore.lambda.exception.BadRequestException;
import com.github.tbe.petstore.lambda.exception.InternalErrorException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CreatePetAction extends AbstractAction implements Action {

	private static final Logger LOGGER = LogManager.getLogger(CreatePetAction.class);

	@Override
	public String handle(JsonObject request, Context lambdaContext, AdminGetUserResult user) throws BadRequestException, InternalErrorException {
		Gson gson = getGson();
		CreatePetRequest inputRequest = gson.fromJson(request, CreatePetRequest.class);

		if (Objects.isNull(inputRequest)) {
			LOGGER.error("Invalid request");
			throw new BadRequestException("Invalid request");
		}

		String username = user.getUsername();
		PetstoreRepository petstoreRepository = new PetstoreRepository();
		Pet pet = new Pet();
		pet.setPetName(inputRequest.getPetName());
		pet.setPetDescription(inputRequest.getPetDescription());

		try {
			petstoreRepository.createPet(username, pet);
		} catch (Exception cause) {
			LOGGER.error(cause.getMessage(), cause);
			throw new InternalErrorException(cause.getMessage(), cause);
		}

		return gson.toJson(new String());
	}
}
