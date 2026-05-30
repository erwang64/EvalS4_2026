import { Component, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LunarBaseService } from '../../../services/lunar-base-service';
import { ActivatedRoute, Router } from '@angular/router';
import { LunarBase } from '../../../model/lunar-base';
import { Observable } from 'rxjs';
import { ConnectionService } from '../../../services/connection-service';

@Component({
  selector: 'app-lunar-base-detail',
  imports: [ReactiveFormsModule],
  templateUrl: './lunar-base-detail.html',
  styleUrl: './lunar-base-detail.scss',
})
export class LunarBaseDetail {

  readonly base: WritableSignal<LunarBase> = signal<LunarBase>(new LunarBase());
  readonly connected: WritableSignal<boolean>;

  form!: FormGroup;
  baseId!: number;

  constructor(
    private baseService: LunarBaseService,
    private connectService: ConnectionService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.connected = connectService.connected;

    this.form = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
      ]),
      sector: new FormControl('', [
        Validators.required,
      ]),
      posX: new FormControl('', [
        Validators.required,
      ]),
      posY: new FormControl('', [
        Validators.required,
      ]),
      maximalCapacity: new FormControl('', [
        Validators.required,
        Validators.min(1),
      ]),
    });

    this.baseId = +this.route.snapshot.params['id'];
    if (!Number.isNaN(this.baseId)) {
      let ret: Observable<LunarBase> = baseService.getBaseById(this.baseId);
      ret.subscribe((data: LunarBase) => {
        this.base.set(data);
        this.form.patchValue(data);
        if (!this.connected()) {
          this.form.disable();
        }
      });
    } else if (!this.connected()) {
      this.router.navigateByUrl("connect");
    }
  }

  validate(): void {
    if (!this.connected()) {
      return;
    }

    let base: LunarBase = new LunarBase();
    base.name = this.form.get("name")?.value;
    base.sector = this.form.get("sector")?.value;
    base.posX = this.form.get("posX")?.value;
    base.posY = this.form.get("posY")?.value;
    base.maximalCapacity = this.form.get("maximalCapacity")?.value;

    if (Number.isNaN(this.baseId)) {
      // Cas de la création
      let resu: Observable<LunarBase> = this.baseService.addBase(base);
      resu.subscribe(() => { this.router.navigateByUrl(""); });
    } else {
      // Cas de l'update
      base.lunarBaseId = this.baseId;
      let resu: Observable<LunarBase> = this.baseService.updateBase(base);
      resu.subscribe(() => { this.router.navigateByUrl(""); });
    }
  }

  cancel(): void {
    this.router.navigateByUrl("");
  }
}
