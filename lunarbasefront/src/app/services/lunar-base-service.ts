import { Injectable, signal, WritableSignal } from '@angular/core';
import { APIService } from './api-service';
import { Observable } from 'rxjs';
import { LunarBase } from '../model/lunar-base';
import { HttpHeaders } from '@angular/common/http';
import { ConnectionService } from './connection-service';

@Injectable({
  providedIn: 'root',
})
export class LunarBaseService {

  readonly allBases: WritableSignal<Array<LunarBase>> =
    signal<Array<LunarBase>>(new Array<LunarBase>());

  readonly ROOT_BASE_URL: string = APIService.ROOT_URL + "/lunar-bases";

  constructor(private apiService: APIService, private connectService: ConnectionService) { }

  getAllBases(): void {
    let url = this.ROOT_BASE_URL + "/all";
    console.info("Call to getAllBases() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    let ret: Observable<Array<LunarBase>> = this.apiService.sendGetRequest<Array<LunarBase>>(url, header);
    ret.subscribe((data: Array<LunarBase>) => {
      this.allBases.set(data);
    });
  }

  getBaseById(id: number): Observable<LunarBase> {
    let url = this.ROOT_BASE_URL + "/" + id;
    console.info("Call to getBaseById() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    return this.apiService.sendGetRequest<LunarBase>(url, header);
  }

  addBase(base: LunarBase): Observable<LunarBase> {
    console.info("Call to addBase() : URL=[" + this.ROOT_BASE_URL + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    return this.apiService.sendPostRequest<LunarBase>(this.ROOT_BASE_URL, base, header);
  }

  updateBase(base: LunarBase): Observable<LunarBase> {
    let url = this.ROOT_BASE_URL + "/" + base.lunarBaseId;
    console.info("Call to updateBase() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    return this.apiService.sendPutRequest<LunarBase>(url, base, header);
  }

  deleteBase(id: number): void {
    let url = this.ROOT_BASE_URL + "/" + id;
    let header: HttpHeaders | null = this.getJwtIfPresent();
    let ret: Observable<LunarBase> = this.apiService.sendDeleteRequest<LunarBase>(url, header);
    ret.subscribe(() => {
      this.allBases.update((bases: Array<LunarBase>) =>
        bases.filter((b: LunarBase) => b.lunarBaseId !== id)
      );
    });
  }

  private getJwtIfPresent(): HttpHeaders | null {
    let header: HttpHeaders | null = null;
    let jwt: string | null = this.connectService.jwt();
    if (jwt != null) {
      header = new HttpHeaders({
        Authorization: jwt,
      });
    }
    return header;
  }
}
