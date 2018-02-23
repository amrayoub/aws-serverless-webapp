package com.github.tbe.petstore.lambda.action.model;

public class CreatePetRequest {

	private String petName;
	private String petDescription;

	public CreatePetRequest() {

	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetDescription() {
		return petDescription;
	}

	public void setPetDescription(String petDescription) {
		this.petDescription = petDescription;
	}
}