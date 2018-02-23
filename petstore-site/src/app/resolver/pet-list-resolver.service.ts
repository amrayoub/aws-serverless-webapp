import { Observable } from "rxjs/Observable";
import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from "@angular/router";
import { Pet } from "../models/pet.model";
import { PetService } from "../service/pet.service";

@Injectable()
export class PetListResolver implements Resolve<Observable<Array<Pet>>> {

	constructor(
		private _petService: PetService
	) {}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Array<Pet>> {
		return this._petService.getPets();
	}
}
