import { Component, Input, WritableSignal } from '@angular/core';
import { LunarBase } from '../../../model/lunar-base';
import { LunarBaseService } from '../../../services/lunar-base-service';
import { ConnectionService } from '../../../services/connection-service';
import { Router } from '@angular/router';

@Component({
  selector: '[app-lunar-base-list-item]',
  imports: [],
  templateUrl: './lunar-base-list-item.html',
  styleUrl: './lunar-base-list-item.scss',
})
export class LunarBaseListItem {
  @Input()
  base!: LunarBase;
  readonly connected: WritableSignal<boolean>;

  constructor(private baseService: LunarBaseService, private connectService: ConnectionService, private router: Router) {
    this.connected = connectService.connected;
  }

  deleteBase(): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette base ?")) {
      this.baseService.deleteBase(this.base.lunarBaseId);
    }
  }

  viewBase(): void {
    this.router.navigateByUrl("bases/detail/" + this.base.lunarBaseId);
  }

  editBase(): void {
    console.info("EDIT BASE : navigation vers l'url bases/detail/" + this.base.lunarBaseId);
    this.router.navigateByUrl("bases/detail/" + this.base.lunarBaseId);
  }

  currentCrewCount(): number {
    if (this.base.crewIds != null && this.base.crewIds.length > 0) {
      return this.base.crewIds.length;
    }
    return this.base.currentCrewCount ?? 0;
  }
}
