import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import Amplify, { Auth } from "aws-amplify";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { Observable } from "rxjs/Observable";
import { fromPromise } from "rxjs/observable/fromPromise";
import { of } from "rxjs/observable/of";
import { catchError, map, tap } from "rxjs/operators";

import { environment } from "./../../environments/environment";

@Injectable()
export class AuthService {
	public loggedIn: BehaviorSubject<boolean>;

	constructor(private _router: Router) {
		Amplify.configure(environment.amplify);
		this.loggedIn = new BehaviorSubject<boolean>(false);
	}

	public signUp(email, password): Observable<any> {
		return fromPromise(Auth.signUp(email, password));
	}

	public confirmSignUp(email, code): Observable<any> {
		return fromPromise(Auth.confirmSignUp(email, code));
	}

	public signIn(email, password): Observable<any> {
		return fromPromise(Auth.signIn(email, password)).pipe(
			tap(() => this.loggedIn.next(true))
		);
	}

	public isAuthenticated(): Observable<boolean> {
		return fromPromise(Auth.currentAuthenticatedUser()).pipe(
			map(result => {
				this.loggedIn.next(true);
				return true;
			}),
			catchError(error => {
				this.loggedIn.next(false);
				return of(false);
			})
		);
	}

	public getCurrentSession(): Observable<any> {
		return fromPromise(Auth.currentSession());
	}

	public signOut() {
		fromPromise(Auth.signOut()).subscribe(
			result => {
				this.loggedIn.next(false);
				this._router.navigate(["/login"]);
			},
			error => console.log(error)
		);
	}
}
