import { Component, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EquipmentService } from '../../../services/equipment-service';
import { ActivatedRoute, Router } from '@angular/router';
import { Equipment } from '../../../model/equipment';
import { Observable } from 'rxjs';
import { LunarBaseService } from '../../../services/lunar-base-service';
import { LunarBase } from '../../../model/lunar-base';

@Component({
  selector: 'app-equipment-detail',
  imports: [ReactiveFormsModule],
  templateUrl: './equipment-detail.html',
  styleUrl: './equipment-detail.scss',
})
export class EquipmentDetail {

  readonly equipment: WritableSignal<Equipment> = signal<Equipment>(new Equipment());
  readonly allBases: WritableSignal<Array<LunarBase>>;

  form!: FormGroup;
  equipmentId!: number;

  // Statuts possibles pour le select
  readonly equipmentStatuses: string[] = ['OPERATIONNEL', 'MAITENANCE', 'HORS_SERVICE'];

  constructor(
    private equipService: EquipmentService,
    private baseService: LunarBaseService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Charger les bases pour le select
    baseService.getAllBases();
    this.allBases = baseService.allBases;

    this.equipmentId = +this.route.snapshot.params['id'];
    if (!Number.isNaN(this.equipmentId)) {
      let ret: Observable<Equipment> = equipService.getEquipmentById(this.equipmentId);
      ret.subscribe((data: Equipment) => {
        this.equipment.set(data);
        this.form.patchValue({
          nameEquiment: data.nameEquiment,
          equipmentStatus: data.equipmentStatus,
        });
      });
    }
    this.form = new FormGroup({
      nameEquiment: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
      ]),
      equipmentStatus: new FormControl('', [
        Validators.required,
      ]),
      lunarBaseId: new FormControl('', [
        Validators.required,
      ]),
    });
  }

  validate(): void {
    let equip: Equipment = new Equipment();
    equip.nameEquiment = this.form.get("nameEquiment")?.value;
    equip.equipmentStatus = this.form.get("equipmentStatus")?.value;
    let lunarBaseId: number = this.form.get("lunarBaseId")?.value;

    if (Number.isNaN(this.equipmentId)) {
      // Cas de la création
      let resu: Observable<Equipment> = this.equipService.addEquipment(equip, lunarBaseId);
      resu.subscribe(() => { this.router.navigateByUrl("equipments"); });
    } else {
      // Cas de l'update
      equip.equipmentId = this.equipmentId;
      let resu: Observable<Equipment> = this.equipService.updateEquipment(equip, lunarBaseId);
      resu.subscribe(() => { this.router.navigateByUrl("equipments"); });
    }
  }

  cancel(): void {
    this.router.navigateByUrl("equipments");
  }
}
