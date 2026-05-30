import { Component, WritableSignal } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../../model/user';
import { ConnectionService } from '../../../services/connection-service';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  readonly connectedUser: WritableSignal<User | null>;
  readonly connected: WritableSignal<boolean>;

  constructor(private service: ConnectionService, private router: Router) {
    this.connectedUser = service.user;
    this.connected = service.connected;
  }

  connect() {
    this.router.navigateByUrl("connect");
  }

  disconnect() {
    this.service.disconnect();
    this.router.navigateByUrl("");
  }

  goToBases() {
    this.router.navigateByUrl("");
  }

  goToCrew() {
    this.router.navigateByUrl("crew");
  }

  goToEquipments() {
    this.router.navigateByUrl("equipments");
  }
}
