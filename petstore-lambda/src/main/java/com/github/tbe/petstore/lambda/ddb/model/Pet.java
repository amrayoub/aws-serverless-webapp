package com.github.tbe.petstore.lambda.ddb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class Pet {

	private String petName;
	private String petDescription;
	
	public Pet() {
		
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
