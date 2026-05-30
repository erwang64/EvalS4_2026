import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConnectionService } from '../../../services/connection-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-connection-form',
  imports: [ReactiveFormsModule],
  templateUrl: './connection-form.html',
  styleUrl: './connection-form.scss',
})
export class ConnectionForm {
  form!: FormGroup;

  constructor(private usersService: ConnectionService, private router: Router) {
    this.form = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(40),
      ]),
    });
  }

  validate(): void {
    this.usersService.login(this.form.get("username")?.value, this.form.get("password")?.value);
    this.router.navigateByUrl("");
  }

  cancel(): void {
    this.router.navigateByUrl("");
  }
}
