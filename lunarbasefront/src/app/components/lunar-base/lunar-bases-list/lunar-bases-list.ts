import { Component, WritableSignal } from '@angular/core';
import { LunarBase } from '../../../model/lunar-base';
import { LunarBaseService } from '../../../services/lunar-base-service';
import { Router } from '@angular/router';
import { LunarBaseListItem } from '../lunar-base-list-item/lunar-base-list-item';
import { ConnectionService } from '../../../services/connection-service';

@Component({
  selector: 'app-lunar-bases-list',
  imports: [LunarBaseListItem],
  templateUrl: './lunar-bases-list.html',
  styleUrl: './lunar-bases-list.scss',
})
export class LunarBasesList {

  readonly allBases: WritableSignal<Array<LunarBase>>;
  readonly connected: WritableSignal<boolean>;

  constructor(private baseService: LunarBaseService, private connectService: ConnectionService, private router: Router) {
    baseService.getAllBases();
    this.allBases = this.baseService.allBases;
    this.connected = connectService.connected;
  }

  addBase(): void {
    this.router.navigateByUrl("bases/detail");
  }
}
