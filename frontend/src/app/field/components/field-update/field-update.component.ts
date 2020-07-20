import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Field, FieldService } from '../../../shared';

@Component({
  selector: 'app-field-update',
  templateUrl: './field-update.component.html',
  styleUrls: ['./field-update.component.scss']
})
export class FieldUpdateComponent implements OnInit {

  fieldId: string;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router,
    private fieldService: FieldService
  ) { }

  ngOnInit(): void {
    this.fieldId = this.route.snapshot.paramMap.get('fieldId');
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      area: ['', [Validators.required]]
    });

    this.findById(this.fieldId);
  }

  findById(id: string) {
    this.fieldService.findById(id)
      .subscribe(
        data => {
          this.form.get('name').setValue(data.name);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  update() {
    if (this.form.invalid) return;

    const field: Field = this.form.value;

    this.fieldService.update(this.get(field))
      .subscribe(
        data => {
          const msg: string = 'Field updated with success!';
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/fields']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  get(data: any): Field {
    return new Field(
      data.name,
      data.area,
      this.fieldId
    );
  }

}
