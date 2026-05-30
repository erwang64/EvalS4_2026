import { Component, Input, WritableSignal } from '@angular/core';
import { CrewMember } from '../../../model/crew-member';
import { CrewMemberService } from '../../../services/crew-member-service';
import { ConnectionService } from '../../../services/connection-service';
import { Router } from '@angular/router';

@Component({
  selector: '[app-crew-member-list-item]',
  imports: [],
  templateUrl: './crew-member-list-item.html',
  styleUrl: './crew-member-list-item.scss',
})
export class CrewMemberListItem {
  @Input()
  member!: CrewMember;
  readonly connected: WritableSignal<boolean>;

  constructor(private crewService: CrewMemberService, private connectService: ConnectionService, private router: Router) {
    this.connected = connectService.connected;
  }

  deleteCrewMember(): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce membre d'équipage ?")) {
      this.crewService.deleteCrewMember(this.member.crewMemberId);
    }
  }

  editCrewMember(): void {
    console.info("EDIT CREW : navigation vers l'url crew/detail/" + this.member.crewMemberId);
    this.router.navigateByUrl("crew/detail/" + this.member.crewMemberId);
  }
}
