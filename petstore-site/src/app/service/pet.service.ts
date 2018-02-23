import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Observable";
import { of } from "rxjs/observable/of";
import { switchMap } from "rxjs/operators";

import { Pet } from "../models/pet.model";
import { environment } from "./../../environments/environment";
import { AuthService } from "./auth.service";

@Injectable()
export class PetService {
	private readonly _CREATE_PET_URL = environment.apiGateWay.petstore.base +
		environment.apiGateWay.petstore.createPet;

	private readonly _LIST_PET_URL = environment.apiGateWay.petstore.base +
		environment.apiGateWay.petstore.listPet;

	constructor(
		private _httpClient: HttpClient,
		private _authService: AuthService
	) {}

	getPets(): Observable<Array<Pet>> {
		return this._authService.getCurrentSession().pipe(
			switchMap(result => {
				const httpOptions = {
					headers: new HttpHeaders({
						"Content-Type": "application/json",
						"Authorization": result.idToken.jwtToken
					})
				};

				return this._httpClient.get<any>(
					this._LIST_PET_URL,
					httpOptions
				);
			})
		);
	}

	createPet(request): Observable<any> {
		return this._authService.getCurrentSession().pipe(
			switchMap(result => {
				const httpOptions = {
					headers: new HttpHeaders({
						"Content-Type": "application/json",
						"Authorization": result.idToken.jwtToken
					})
				};

				return this._httpClient.post<any>(
					this._CREATE_PET_URL,
					request,
					httpOptions
				);
			})
		);
	}
}
