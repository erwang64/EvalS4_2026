import { Routes } from '@angular/router';
import { LunarBasesList } from './components/lunar-base/lunar-bases-list/lunar-bases-list';
import { LunarBaseDetail } from './components/lunar-base/lunar-base-detail/lunar-base-detail';
import { CrewMembersList } from './components/crew-member/crew-members-list/crew-members-list';
import { CrewMemberDetail } from './components/crew-member/crew-member-detail/crew-member-detail';
import { EquipmentsList } from './components/equipment/equipments-list/equipments-list';
import { EquipmentDetail } from './components/equipment/equipment-detail/equipment-detail';
import { ConnectionForm } from './components/technical/connection-form/connection-form';
import { NotFound } from './components/technical/not-found/not-found';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: '', component: LunarBasesList },
    { path: 'connect', component: ConnectionForm },
    { path: 'bases/detail', component: LunarBaseDetail, canActivate: [authGuard] },
    { path: 'bases/detail/:id', component: LunarBaseDetail },
    { path: 'crew', component: CrewMembersList },
    { path: 'crew/detail', component: CrewMemberDetail, canActivate: [authGuard] },
    { path: 'crew/detail/:id', component: CrewMemberDetail, canActivate: [authGuard] },
    { path: 'equipments', component: EquipmentsList },
    { path: 'equipments/detail', component: EquipmentDetail, canActivate: [authGuard] },
    { path: 'equipments/detail/:id', component: EquipmentDetail, canActivate: [authGuard] },
    { path: '**', component: NotFound }
];
