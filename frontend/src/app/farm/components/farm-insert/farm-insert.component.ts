import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Farm, FarmService } from '../../../shared';

@Component({
  selector: 'app-farm-insert',
  templateUrl: './farm-insert.component.html',
  styleUrls: ['./farm-insert.component.scss']
})
export class FarmInsertComponent implements OnInit {

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

  insert() {
  	if (this.form.invalid) return;

    this.farmService.insert(this.getObjectFromForm(this.form.value))
      .subscribe(
        data => {
          const msg: string = 'Farm inserted with success!';
          this.snackBar.open(msg, 'Success');
          this.router.navigate(['/farms']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  getObjectFromForm(data: any): Farm {
    return new Farm(
      data.name,
      null
    );
  }

}
