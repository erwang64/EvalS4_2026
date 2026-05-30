import { Component, WritableSignal } from '@angular/core';
import { Equipment } from '../../../model/equipment';
import { EquipmentService } from '../../../services/equipment-service';
import { Router } from '@angular/router';
import { EquipmentListItem } from '../equipment-list-item/equipment-list-item';
import { ConnectionService } from '../../../services/connection-service';

@Component({
  selector: 'app-equipments-list',
  imports: [EquipmentListItem],
  templateUrl: './equipments-list.html',
  styleUrl: './equipments-list.scss',
})
export class EquipmentsList {

  readonly allEquipments: WritableSignal<Array<Equipment>>;
  readonly connected: WritableSignal<boolean>;

  constructor(private equipService: EquipmentService, private connectService: ConnectionService, private router: Router) {
    equipService.getAllEquipments();
    this.allEquipments = this.equipService.allEquipments;
    this.connected = connectService.connected;
  }

  addEquipment(): void {
    this.router.navigateByUrl("equipments/detail");
  }
}
