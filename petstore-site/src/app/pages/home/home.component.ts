import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material";
import { ActivatedRoute } from "@angular/router";

import { NewPetDialogComponent } from "../../dialogs/newpet/newpet.component";
import { Pet } from "../../models/pet.model";
import { PetService } from "../../service/pet.service";

@Component({
	selector: "app-home",
	templateUrl: "./home.component.html",
	styleUrls: ["./home.component.css"]
})
export class HomeComponent implements OnInit {
	pets: Array<Pet>;

	constructor(
		public dialog: MatDialog,
		private readonly _activatedRoute: ActivatedRoute,
		private readonly _petService: PetService
	) {}

	ngOnInit() {
		this._activatedRoute.data.subscribe(result => {
			this.pets = result.pets;
		});
	}

	openDialog(): void {
		const dialogRef = this.dialog.open(NewPetDialogComponent);

		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				const pet = result;
				this._petService
					.createPet(pet)
					.subscribe(
						data => this.pets.push(pet),
						error => console.log(error)
					);
			}
		});
	}
}
