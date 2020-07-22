import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Field, FieldService, Farm, ProductionService } from '../../../shared';
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
    private productionService: ProductionService,
    private sharedService: SharedService
  ) { }

  private getObject(data: any): Field {
    return new Field(
      data.name,
      data.area,
      this.farmCurrent.id,
      this.fieldId
    );
  }

  ngOnInit(): void {
    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);

    this.findById();

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      area: ['', [Validators.required]]
    });
  }

  findById() {
    this.fieldService.findById(this.route.snapshot.paramMap.get('fieldId'))
      .subscribe(
        data => {
          this.fieldId = data.id;
          this.form.get('name').setValue(data.name);
          this.form.get('area').setValue(data.area);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  update() {
    if (this.form.invalid) return;

    this.fieldService.update(this.getObject(this.form.value))
      .subscribe(
        data => {
          this.snackBar.open('Field updated with success!', 'Success');
          this.router.navigate(['/fields']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  deleteById() {
    this.fieldService.deleteById(this.fieldId)
      .subscribe(
        data => {
          this.snackBar.open('Field deleted with success!', 'Success');
          this.router.navigate(['/fields']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  getProductivity() {
    this.productionService.productivityByFieldId(this.fieldId)
      .subscribe(
        data => { this.snackBar.open(Number(data).toLocaleString('en', { maximumFractionDigits: 2 }), 'Productivity'); },
        err => { this.snackBar.open(err.error.message, 'Error'); }
      );
  }

}
