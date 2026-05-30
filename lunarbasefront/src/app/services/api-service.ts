import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, retry, throwError } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class APIService {

  public static readonly ROOT_URL: string = environment.apiUrl;
  public static readonly DEFAULT_HEADERS: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private httpClient: HttpClient) { }

  public sendGetRequest<T>(url: string, headers: HttpHeaders | null)
    : Observable<T> {
    if (headers == null) {
      headers = APIService.DEFAULT_HEADERS;
    }
    let httpOptions: { observe: 'body'; responseType: 'json'; headers: HttpHeaders } = {
      observe: 'body',
      responseType: 'json',
      headers: headers
    };
    console.debug("sendGetRequest url=[" + url + "]");
    return this.httpClient.get<T>(url, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }

  public sendDeleteRequest<T>(url: string, headers: HttpHeaders | null)
    : Observable<T> {
    if (headers == null) {
      headers = APIService.DEFAULT_HEADERS;
    }
    let httpOptions: { observe: 'body'; responseType: 'json'; headers: HttpHeaders } = {
      observe: 'body',
      responseType: 'json',
      headers: headers
    };
    console.debug("sendDeleteRequest url=[" + url + "]");
    return this.httpClient.delete<T>(url, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }

  sendPostRequest<T>(url: string,
    data: T,
    headers: HttpHeaders | null): Observable<T> {
    if (headers == null) {
      headers = APIService.DEFAULT_HEADERS;
    }
    let httpOptions: { observe: 'body'; responseType: 'json'; headers: HttpHeaders } = {
      observe: 'body',
      responseType: 'json',
      headers: headers
    };
    console.debug("sendPostRequest url=[" + url + "] / data=[" + data + "]");
    return this.httpClient.post<T>(url, data, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }

  sendPostRequestWithResponseHeaders<T>(url: string,
    data: T,
    headers: HttpHeaders | null): Observable<HttpResponse<T>> {
    if (headers == null) {
      headers = APIService.DEFAULT_HEADERS;
    }
    const httpOptions: {
      headers: HttpHeaders;
      observe: 'response';
      responseType: 'json';
    } = {
      headers: headers,
      observe: 'response',
      responseType: 'json'
    };
    console.debug("sendPostRequestWithResponseHeaders url=[" + url + "] / data=[" + data + "]");
    return this.httpClient.post<T>(url, data, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }

  sendPutRequest<T>(url: string,
    data: T,
    headers: HttpHeaders | null): Observable<T> {
    if (headers == null) {
      headers = APIService.DEFAULT_HEADERS;
    }
    let httpOptions: { observe: 'body'; responseType: 'json'; headers: HttpHeaders } = {
      observe: 'body',
      responseType: 'json',
      headers: headers
    };
    console.log("sendPutRequest url=[" + url + "] / data=[" + data + "]");
    return this.httpClient.put<T>(url, data, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }

  httpError(error: HttpErrorResponse) {
    let msg: string = "";
    if (typeof ErrorEvent !== 'undefined' && error.error instanceof ErrorEvent) {
      // Client side error
      msg = error.error.message;
    } else {
      // API side error
      msg = "Status : " + error.status + "\n" +
        "Message : " + error.message + "\n" +
        "Details : " + JSON.stringify(error.error);
    }
    console.log(msg);
    return throwError(() => error);
  }

}
