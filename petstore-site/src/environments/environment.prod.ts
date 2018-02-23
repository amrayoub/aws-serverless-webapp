export const environment = {
	production: true,
	amplify: {
		Auth: {
			region: "<your_region>",
			userPoolId: "<your_userPoolId>",
			userPoolWebClientId: "<your_userPoolWebClientId>"
		}
	},
	apiGateWay: {
		petstore: {
			base: "https://<your_apiGateWay_url>/dev",
			createPet: "/create-pet",
			listPet: "/list-pet"
		}
	}
};
