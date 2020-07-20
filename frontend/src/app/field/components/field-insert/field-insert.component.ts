import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Field, FieldService } from '../../../shared';

@Component({
  selector: 'app-field-insert',
  templateUrl: './field-insert.component.html',
  styleUrls: ['./field-insert.component.scss']
})
export class FieldInsertComponent implements OnInit {

  form: FormGroup;

  constructor(
  	private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private fieldService: FieldService
  ) { }

  ngOnInit(): void {
  	this.form = this.fb.group({
  		name: ['', [Validators.required, Validators.minLength(3)]],
      area: ['', [Validators.required]]
  	});
  }

  insert() {
  	if (this.form.invalid) return;

    const field: Field = this.form.value;

    this.fieldService.insert(field)
      .subscribe(
        data => {
          const msg: string = 'Field inserted with success!';
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/fields']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

}
