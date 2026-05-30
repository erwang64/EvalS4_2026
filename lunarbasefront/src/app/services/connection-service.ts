import { Injectable, signal, WritableSignal } from '@angular/core';
import { APIService } from './api-service';
import { User } from '../model/user';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { catchError, retry, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ConnectionService {

  readonly user: WritableSignal<User | null> = signal<User | null>(null);
  readonly jwt: WritableSignal<string | null> = signal<string | null>(null);
  readonly connected: WritableSignal<boolean> = signal<boolean>(false);

  readonly ROOT_USER_URL: string = APIService.ROOT_URL + "/users";

  constructor(private apiService: APIService, private httpClient: HttpClient) { }

  login(username: string, password: string): void {
    const body: { username: string; password: string } = { username, password };
    let url = this.ROOT_USER_URL + "/login";
    const httpOptions: {
      headers: HttpHeaders;
      observe: 'response';
      responseType: 'json';
    } = {
      headers: APIService.DEFAULT_HEADERS,
      observe: 'response',
      responseType: 'json'
    };
    let obs: Observable<HttpResponse<object>> = this.httpClient.post<object>(url, body, httpOptions)
      .pipe(retry(1), catchError((error: HttpErrorResponse) => {
        console.log("Erreur login : " + error.message);
        return throwError(() => new Error(error.message));
      }));
    obs.subscribe((data: HttpResponse<object>) => {
      let headers: HttpHeaders = data.headers;
      // Le backend renvoie 204 No Content + header Authorization: Bearer <jwt>
      this.jwt.set(headers.get("Authorization"));
      // On crée un user avec les infos qu'on a (le backend ne renvoie pas le body sur login)
      const connectedUser: User = new User();
      connectedUser.username = username;
      this.user.set(connectedUser);
      this.connected.set(true);
      console.log("**************************************************");
      console.log("JWT : [" + this.jwt() + "]");
      console.log("User : [" + this.user()?.username + "]");
      console.log("**************************************************");
    });
  }

  disconnect(): void {
    this.user.set(null);
    this.jwt.set(null);
    this.connected.set(false);
    console.log("**************************************************");
    console.log("DISCONNECTED");
    console.log("**************************************************");
  }

  isAdmin(): boolean {
    const currentUser: User | null = this.user();
    return currentUser != null && currentUser.role === 'ADMIN';
  }
}
