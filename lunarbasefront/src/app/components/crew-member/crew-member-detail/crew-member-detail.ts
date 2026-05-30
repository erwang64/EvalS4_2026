import { Component, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CrewMemberService } from '../../../services/crew-member-service';
import { ActivatedRoute, Router } from '@angular/router';
import { CrewMember } from '../../../model/crew-member';
import { Observable } from 'rxjs';
import { LunarBaseService } from '../../../services/lunar-base-service';
import { LunarBase } from '../../../model/lunar-base';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-crew-member-detail',
  imports: [ReactiveFormsModule],
  templateUrl: './crew-member-detail.html',
  styleUrl: './crew-member-detail.scss',
})
export class CrewMemberDetail {

  readonly member: WritableSignal<CrewMember> = signal<CrewMember>(new CrewMember());
  readonly allBases: WritableSignal<Array<LunarBase>>;
  readonly errorMessage: WritableSignal<string | null> = signal<string | null>(null);

  form!: FormGroup;
  memberId!: number;

  // Rôles possibles pour le select
  readonly crewRoles: string[] = ['INGENIEUR', 'MEDECIN', 'PILOTE', 'MECANICIEN'];

  constructor(
    private crewService: CrewMemberService,
    private baseService: LunarBaseService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Charger les bases pour le select
    baseService.getAllBases();
    this.allBases = baseService.allBases;

    this.memberId = +this.route.snapshot.params['id'];
    if (!Number.isNaN(this.memberId)) {
      let ret: Observable<CrewMember> = crewService.getCrewMemberById(this.memberId);
      ret.subscribe((data: CrewMember) => {
        this.member.set(data);
        this.form.patchValue({
          firstName: data.firstName,
          lastName: data.lastName,
          crewRole: data.crewRole,
          spaceSuitSize: data.spaceSuit?.size,
          spaceSuitModel: data.spaceSuit?.model,
        });
      });
    }
    this.form = new FormGroup({
      firstName: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
      ]),
      crewRole: new FormControl('', [
        Validators.required,
      ]),
      lunarBaseId: new FormControl('', [
        Validators.required,
      ]),
      spaceSuitSize: new FormControl('', [
        Validators.required,
        Validators.min(1),
      ]),
      spaceSuitModel: new FormControl('', [
        Validators.required,
      ]),
    });
  }

  validate(): void {
    this.errorMessage.set(null);
    let lunarBaseId: number = +this.form.get("lunarBaseId")?.value;
    const selectedBase: LunarBase | undefined = this.allBases().find(
      (b: LunarBase) => b.lunarBaseId === lunarBaseId
    );

    if (Number.isNaN(this.memberId) && selectedBase != null && this.isBaseFull(selectedBase)) {
      this.errorMessage.set(
        "Impossible d'ajouter ce membre : la base « " + selectedBase.name + " » a atteint sa capacité maximale (" +
        selectedBase.maximalCapacity + " personnes)."
      );
      return;
    }

    let member: CrewMember = new CrewMember();
    member.firstName = this.form.get("firstName")?.value;
    member.lastName = this.form.get("lastName")?.value;
    member.crewRole = this.form.get("crewRole")?.value;
    member.spaceSuit = {
      spaceSuitId: 0,
      size: this.form.get("spaceSuitSize")?.value,
      model: this.form.get("spaceSuitModel")?.value,
    };

    if (Number.isNaN(this.memberId)) {
      // Cas de la création
      let resu: Observable<CrewMember> = this.crewService.addCrewMember(member, lunarBaseId);
      resu.subscribe({
        next: () => { this.router.navigateByUrl("crew"); },
        error: (err: unknown) => { this.errorMessage.set(this.extractErrorMessage(err)); },
      });
    } else {
      // Cas de l'update
      member.crewMemberId = this.memberId;
      let resu: Observable<CrewMember> = this.crewService.updateCrewMember(member, lunarBaseId);
      resu.subscribe({
        next: () => { this.router.navigateByUrl("crew"); },
        error: (err: unknown) => { this.errorMessage.set(this.extractErrorMessage(err)); },
      });
    }
  }

  cancel(): void {
    this.router.navigateByUrl("crew");
  }

  currentCrewCount(base: LunarBase): number {
    if (base.crewIds != null && base.crewIds.length > 0) {
      return base.crewIds.length;
    }
    return base.currentCrewCount ?? 0;
  }

  isBaseFull(base: LunarBase): boolean {
    return this.currentCrewCount(base) >= base.maximalCapacity;
  }

  private extractErrorMessage(err: unknown): string {
    if (err instanceof HttpErrorResponse && err.error?.message) {
      return err.error.message;
    }
    if (err instanceof Error && err.message.includes('capacité maximale')) {
      return "Affectation impossible : la base a atteint sa capacité maximale de membres.";
    }
    return "Une erreur est survenue lors de l'enregistrement du membre d'équipage.";
  }
}
