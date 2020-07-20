import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Field, FieldService, Farm } from '../../../shared';
import { SharedService } from '../../services';

@Component({
  selector: 'app-field-update',
  templateUrl: './field-update.component.html',
  styleUrls: ['./field-update.component.scss']
})
export class FieldUpdateComponent implements OnInit {

  form: FormGroup;
  fieldId: string;
  farmCurrent: Farm;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router,
    private fieldService: FieldService,
    private sharedService: SharedService
  ) { }

  ngOnInit(): void {
    this.fieldId = this.route.snapshot.paramMap.get('fieldId');
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      area: ['', [Validators.required]]
    });

    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);
    this.findById(this.fieldId);
  }

  findById(id: string) {
    this.fieldService.findById(id)
      .subscribe(
        data => {
          this.form.get('name').setValue(data.name);
          this.form.get('area').setValue(data.area);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  update() {
    if (this.form.invalid) return;

    console.log(this.farmCurrent.id)
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

  deleteById() {
    this.fieldService.deleteById(this.fieldId)
      .subscribe(
        data => {
          const msg: string = "Field deleted with success!";
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
      this.farmCurrent.id,
      this.fieldId
    );
  }

}
