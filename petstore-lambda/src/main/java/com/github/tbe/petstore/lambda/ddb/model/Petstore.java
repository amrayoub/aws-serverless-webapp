package com.github.tbe.petstore.lambda.ddb.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Petstore")
public class Petstore {

	private String username;
	private List<Pet> pets;
	
	public Petstore() {
		
	}

	@DynamoDBHashKey(attributeName = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@DynamoDBAttribute(attributeName = "pets")
	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}
}
