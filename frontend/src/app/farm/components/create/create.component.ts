import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Farm, FarmService } from '../../../shared';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateComponent implements OnInit {

  form: FormGroup;

  constructor(
  	private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private farmService: FarmService
  ) { }

  ngOnInit(): void {
  	this.form = this.fb.group({
  		name: ['', [Validators.required, Validators.minLength(3)]]
  	});
  }

  create() {
  	if (this.form.invalid) {
      return;
    }

    const farm: Farm = this.form.value;

    this.farmService.create(farm)
      .subscribe(
        data => {
          const msg: string = 'Farm created with success!';
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/farms']);
        },
        err => {
          let msg: string = 'Try again in minutes.';
          if (err.status == 400) {
            msg = err.error.errors.join(' ');
          }
          this.snackBar.open(msg, "Error");
        }
      );
  }

}
