import { Component, Inject, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { Pet } from "../../models/pet.model";

@Component({
	selector: "app-new-pet-dialog",
	templateUrl: "./newpet.component.html",
	styleUrls: ["./newpet.component.css"]
})
export class NewPetDialogComponent implements OnInit {
	public newPetForm: FormGroup;

	constructor(
		private formBuilder: FormBuilder,
		public dialogRef: MatDialogRef<NewPetDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: Pet
	) {}

	ngOnInit() {
		this.initForm();
	}

	initForm() {
		this.newPetForm = this.formBuilder.group({
			petName: ["", Validators.required],
			petDescription: ["", Validators.required]
		});
	}

	onNoClick(): void {
		this.dialogRef.close();
	}

	onSubmitCreatePet(value: Pet) {
		this.dialogRef.close(value);
	}
}
