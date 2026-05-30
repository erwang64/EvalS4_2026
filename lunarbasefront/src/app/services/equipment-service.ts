import { Injectable, signal, WritableSignal } from '@angular/core';
import { APIService } from './api-service';
import { Observable } from 'rxjs';
import { Equipment } from '../model/equipment';
import { HttpHeaders } from '@angular/common/http';
import { ConnectionService } from './connection-service';

@Injectable({
  providedIn: 'root',
})
export class EquipmentService {

  readonly allEquipments: WritableSignal<Array<Equipment>> =
    signal<Array<Equipment>>(new Array<Equipment>());

  readonly ROOT_EQUIP_URL: string = APIService.ROOT_URL + "/equipments";

  constructor(private apiService: APIService, private connectService: ConnectionService) { }

  getAllEquipments(): void {
    let url = this.ROOT_EQUIP_URL + "/all";
    console.info("Call to getAllEquipments() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    let ret: Observable<Array<Equipment>> = this.apiService.sendGetRequest<Array<Equipment>>(url, header);
    ret.subscribe((data: Array<Equipment>) => {
      this.allEquipments.set(data);
    });
  }

  getEquipmentById(id: number): Observable<Equipment> {
    let url = this.ROOT_EQUIP_URL + "/" + id;
    console.info("Call to getEquipmentById() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    return this.apiService.sendGetRequest<Equipment>(url, header);
  }

  addEquipment(equipment: Equipment, lunarBaseId: number): Observable<Equipment> {
    console.info("Call to addEquipment() : URL=[" + this.ROOT_EQUIP_URL + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    const requestBody: { nameEquiment: string; equipmentStatus: string; lunarBaseId: number } = {
      nameEquiment: equipment.nameEquiment,
      equipmentStatus: equipment.equipmentStatus,
      lunarBaseId: lunarBaseId
    };
    return this.apiService.sendPostRequest<Equipment>(this.ROOT_EQUIP_URL, requestBody as unknown as Equipment, header);
  }

  updateEquipment(equipment: Equipment, lunarBaseId: number): Observable<Equipment> {
    let url = this.ROOT_EQUIP_URL + "/" + equipment.equipmentId;
    console.info("Call to updateEquipment() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    const requestBody: { nameEquiment: string; equipmentStatus: string; lunarBaseId: number } = {
      nameEquiment: equipment.nameEquiment,
      equipmentStatus: equipment.equipmentStatus,
      lunarBaseId: lunarBaseId
    };
    return this.apiService.sendPutRequest<Equipment>(url, requestBody as unknown as Equipment, header);
  }

  deleteEquipment(id: number): void {
    let url = this.ROOT_EQUIP_URL + "/" + id;
    let header: HttpHeaders | null = this.getJwtIfPresent();
    let ret: Observable<Equipment> = this.apiService.sendDeleteRequest<Equipment>(url, header);
    ret.subscribe(() => {
      this.allEquipments.update((equipments: Array<Equipment>) =>
        equipments.filter((e: Equipment) => e.equipmentId !== id)
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
