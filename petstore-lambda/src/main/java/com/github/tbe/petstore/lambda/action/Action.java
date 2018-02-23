package com.github.tbe.petstore.lambda.action;

import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.github.tbe.petstore.lambda.exception.BadRequestException;
import com.github.tbe.petstore.lambda.exception.InternalErrorException;
import com.google.gson.JsonObject;

public interface Action {

	String handle(JsonObject request, Context lambdaContext, AdminGetUserResult user) throws BadRequestException, InternalErrorException;
}
