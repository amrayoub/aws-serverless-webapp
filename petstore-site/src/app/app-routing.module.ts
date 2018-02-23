import { NgModule } from "@angular/core";
import { Routes, RouterModule, CanActivate } from "@angular/router";
import { LoginComponent } from "./pages/login/login.component";
import { SignupComponent } from "./pages/signup/signup.component";
import { HomeComponent } from "./pages/home/home.component";
import { AuthGuard } from "./guard/auth.guard";
import { PetListResolver } from "./resolver/pet-list-resolver.service";

const routes: Routes = [
	{
		path: "home",
		component: HomeComponent,
		canActivate: [AuthGuard],
		resolve: { pets: PetListResolver }
	},
	{ path: "login", component: LoginComponent },
	{ path: "signup", component: SignupComponent },
	{ path: "", redirectTo: "home", pathMatch: "full" }
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule],
	providers: [AuthGuard, PetListResolver]
})
export class AppRoutingModule {}
