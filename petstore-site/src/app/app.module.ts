import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from "@angular/core";

import {
	MatButtonModule,
	MatCardModule,
	MatDialogModule,
	MatExpansionModule,
	MatInputModule,
	MatToolbarModule
} from "@angular/material";

import { AppComponent } from "./app.component";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { AuthService } from "./service/auth.service";
import { SignupComponent } from "./pages/signup/signup.component";
import { HomeComponent } from "./pages/home/home.component";
import { LoginComponent } from "./pages/login/login.component";
import { AppRoutingModule } from "./app-routing.module";
import { NewPetDialogComponent } from "./dialogs/newpet/newpet.component";
import { PetService } from "./service/pet.service";

@NgModule({
	declarations: [
		AppComponent,
		SignupComponent,
		LoginComponent,
		HomeComponent,
		NewPetDialogComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		ReactiveFormsModule,
		HttpClientModule,
		AppRoutingModule,
		MatButtonModule,
		MatCardModule,
		MatDialogModule,
		MatExpansionModule,
		MatInputModule,
		MatToolbarModule
	],
	providers: [AuthService, PetService],
	bootstrap: [AppComponent],
	entryComponents: [NewPetDialogComponent]
})
export class AppModule {}
