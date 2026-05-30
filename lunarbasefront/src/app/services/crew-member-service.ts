import { Injectable, signal, WritableSignal } from '@angular/core';
import { APIService } from './api-service';
import { Observable } from 'rxjs';
import { CrewMember } from '../model/crew-member';
import { HttpHeaders } from '@angular/common/http';
import { ConnectionService } from './connection-service';

@Injectable({
  providedIn: 'root',
})
export class CrewMemberService {

  readonly allCrewMembers: WritableSignal<Array<CrewMember>> =
    signal<Array<CrewMember>>(new Array<CrewMember>());

  readonly ROOT_CREW_URL: string = APIService.ROOT_URL + "/crew-members";

  constructor(private apiService: APIService, private connectService: ConnectionService) { }

  getAllCrewMembers(): void {
    let url = this.ROOT_CREW_URL + "/all";
    console.info("Call to getAllCrewMembers() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    let ret: Observable<Array<CrewMember>> = this.apiService.sendGetRequest<Array<CrewMember>>(url, header);
    ret.subscribe((data: Array<CrewMember>) => {
      this.allCrewMembers.set(data);
    });
  }

  getCrewMemberById(id: number): Observable<CrewMember> {
    let url = this.ROOT_CREW_URL + "/" + id;
    console.info("Call to getCrewMemberById() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    return this.apiService.sendGetRequest<CrewMember>(url, header);
  }

  addCrewMember(member: CrewMember, lunarBaseId: number): Observable<CrewMember> {
    console.info("Call to addCrewMember() : URL=[" + this.ROOT_CREW_URL + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    // On doit envoyer le lunarBaseId dans le body (comme le backend attend un CrewMemberRequest)
    const requestBody: { firstName: string; lastName: string; crewRole: string; spaceSuit: { size: number; model: string }; lunarBaseId: number } = {
      firstName: member.firstName,
      lastName: member.lastName,
      crewRole: member.crewRole,
      spaceSuit: {
        size: member.spaceSuit.size,
        model: member.spaceSuit.model
      },
      lunarBaseId: lunarBaseId
    };
    return this.apiService.sendPostRequest<CrewMember>(this.ROOT_CREW_URL, requestBody as unknown as CrewMember, header);
  }

  updateCrewMember(member: CrewMember, lunarBaseId: number): Observable<CrewMember> {
    let url = this.ROOT_CREW_URL + "/" + member.crewMemberId;
    console.info("Call to updateCrewMember() : URL=[" + url + "]");
    let header: HttpHeaders | null = this.getJwtIfPresent();
    const requestBody: { firstName: string; lastName: string; crewRole: string; spaceSuit: { size: number; model: string }; lunarBaseId: number } = {
      firstName: member.firstName,
      lastName: member.lastName,
      crewRole: member.crewRole,
      spaceSuit: {
        size: member.spaceSuit.size,
        model: member.spaceSuit.model
      },
      lunarBaseId: lunarBaseId
    };
    return this.apiService.sendPutRequest<CrewMember>(url, requestBody as unknown as CrewMember, header);
  }

  deleteCrewMember(id: number): void {
    let url = this.ROOT_CREW_URL + "/" + id;
    let header: HttpHeaders | null = this.getJwtIfPresent();
    let ret: Observable<CrewMember> = this.apiService.sendDeleteRequest<CrewMember>(url, header);
    ret.subscribe(() => {
      this.allCrewMembers.update((members: Array<CrewMember>) =>
        members.filter((m: CrewMember) => m.crewMemberId !== id)
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
