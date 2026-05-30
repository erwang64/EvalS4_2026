import { Component, WritableSignal } from '@angular/core';
import { CrewMember } from '../../../model/crew-member';
import { CrewMemberService } from '../../../services/crew-member-service';
import { Router } from '@angular/router';
import { CrewMemberListItem } from '../crew-member-list-item/crew-member-list-item';
import { ConnectionService } from '../../../services/connection-service';

@Component({
  selector: 'app-crew-members-list',
  imports: [CrewMemberListItem],
  templateUrl: './crew-members-list.html',
  styleUrl: './crew-members-list.scss',
})
export class CrewMembersList {

  readonly allCrewMembers: WritableSignal<Array<CrewMember>>;
  readonly connected: WritableSignal<boolean>;

  constructor(private crewService: CrewMemberService, private connectService: ConnectionService, private router: Router) {
    crewService.getAllCrewMembers();
    this.allCrewMembers = this.crewService.allCrewMembers;
    this.connected = connectService.connected;
  }

  addCrewMember(): void {
    this.router.navigateByUrl("crew/detail");
  }
}
