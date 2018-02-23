import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../service/auth.service";

@Component({
	selector: "app-signup",
	templateUrl: "./signup.component.html",
	styleUrls: ["./signup.component.css"]
})
export class SignupComponent implements OnInit {
	public signupForm: FormGroup;
	public confirmationForm: FormGroup;
	public successfullySignup: boolean;

	constructor(
		private formBuilder: FormBuilder,
		private router: Router,
		private auth: AuthService
	) {}

	ngOnInit() {
		this.initForm();
	}

	initForm() {
		this.signupForm = this.formBuilder.group({
			email: ["", [Validators.required, Validators.email]],
			password: ["", Validators.required]
		});
		this.confirmationForm = this.formBuilder.group({
			email: ["", [Validators.required, Validators.email]],
			confirmationCode: ["", Validators.required]
		});
	}

	onSubmitSignup(value: any) {
		const email = value.email,
			password = value.password;
		this.auth.signUp(email, password).subscribe(
			result => {
				this.successfullySignup = true;
			},
			error => {
				console.log(error);
			}
		);
	}

	onSubmitConfirmation(value: any) {
		const email = value.email,
			confirmationCode = value.confirmationCode;
		this.auth.confirmSignUp(email, confirmationCode).subscribe(
			result => {
				this.router.navigate(["/login"]);
			},
			error => {
				console.log(error);
			}
		);
	}
}
