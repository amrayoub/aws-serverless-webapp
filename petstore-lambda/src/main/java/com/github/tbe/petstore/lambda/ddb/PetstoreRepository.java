package com.github.tbe.petstore.lambda.ddb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.github.tbe.petstore.lambda.ddb.model.Pet;
import com.github.tbe.petstore.lambda.ddb.model.Petstore;

public class PetstoreRepository {

	private DynamoDBMapper getMapper() throws IOException {
		Properties properties = new Properties();

		try (InputStream stream = PetstoreRepository.class.getClassLoader().getResourceAsStream("dynamodb.properties")) {
			properties.load(stream);
		}

		String regionName = properties.getProperty("dynamodb.region");

		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.fromName(regionName))
				.build();

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		return mapper;
	}

	public void createPet(String username, Pet pet) throws IOException {
		DynamoDBMapper mapper = getMapper();
		Petstore petstore = mapper.load(Petstore.class, username);

		if (Objects.isNull(petstore)) {
			petstore = new Petstore();
			petstore.setUsername(username);
			petstore.setPets(new ArrayList<>());
		}

		petstore.getPets().add(pet);
		mapper.save(petstore);
	}

	public List<Pet> getPets(String username) throws IOException {
		DynamoDBMapper mapper = getMapper();
		Petstore petstore = mapper.load(Petstore.class, username);
		List<Pet> pets = new ArrayList<>();

		if (Objects.nonNull(petstore)) {
			pets.addAll(petstore.getPets());
		}

		return pets;
	}
}
