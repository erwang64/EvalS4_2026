import { Component, Input, WritableSignal } from '@angular/core';
import { Equipment } from '../../../model/equipment';
import { EquipmentService } from '../../../services/equipment-service';
import { ConnectionService } from '../../../services/connection-service';
import { Router } from '@angular/router';

@Component({
  selector: '[app-equipment-list-item]',
  imports: [],
  templateUrl: './equipment-list-item.html',
  styleUrl: './equipment-list-item.scss',
})
export class EquipmentListItem {
  @Input()
  equip!: Equipment;
  readonly connected: WritableSignal<boolean>;

  constructor(private equipService: EquipmentService, private connectService: ConnectionService, private router: Router) {
    this.connected = connectService.connected;
  }

  deleteEquipment(): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cet équipement ?")) {
      this.equipService.deleteEquipment(this.equip.equipmentId);
    }
  }

  editEquipment(): void {
    console.info("EDIT EQUIP : navigation vers l'url equipments/detail/" + this.equip.equipmentId);
    this.router.navigateByUrl("equipments/detail/" + this.equip.equipmentId);
  }
}
