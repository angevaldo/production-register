import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Production, ProductionService, Farm, FarmService, FieldService, Field } from '../../../shared';
import { SharedService } from '../../services';

@Component({
  selector: 'app-production-insert',
  templateUrl: './production-insert.component.html',
  styleUrls: ['./production-insert.component.scss']
})
export class ProductionInsertComponent implements OnInit {

  form: FormGroup;
  farms: Farm[];
  farmCurrent: Farm;
  fields: Field[];
  fieldCurrent: Field;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private productionService: ProductionService,
    private farmService: FarmService,
    private fieldService: FieldService,
    private sharedService: SharedService
  ) { }

  private populateSelectFarms() {
    this.farmService.findAll()
      .subscribe(
        data => { this.farms = data as Farm[]; },
        err => { this.snackBar.open(err.error.message, 'Error'); }
      );
  }

  private populateSelectFields() {
    if (this.farmCurrent.id) {
      this.fieldService.findByFarmId(this.farmCurrent.id)
        .subscribe(
          data => { this.fields = data as Field[]; },
          err => { this.snackBar.open(err.error.message, 'Error'); }
        );
    }
  }

  private getObject(data: any): Production {
    return new Production(
      data.value,
      data.fieldId,
      null
    );
  }

  ngOnInit(): void {
    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);
    this.sharedService.sharedField.subscribe(fieldCurrent => this.fieldCurrent = fieldCurrent);

    this.populateSelectFarms();
    this.populateSelectFields();

    this.form = this.fb.group({
      value: ['', [Validators.required]],
      farmId: [this.farmCurrent.id, [Validators.required]],
      fieldId: [this.fieldCurrent.id, [Validators.required]]
    });
  }

  onChangeFarmCurrent(farmId: string) {
    const farm: Farm = this.farms.find(s => s.id == farmId);
    this.farmCurrent.id = farm.id;
    this.farmCurrent.name = farm.name;
    this.sharedService.nextFarm(this.farmCurrent);

    this.fieldCurrent = new Field('?', null, farmId);
    this.sharedService.nextField(this.fieldCurrent);

    this.populateSelectFields();
  }

  onChangeFieldCurrent(fieldId: string) {
    if (fieldId == null || this.fields == null) return;

    const field: Field = this.fields.find(s => s.id == fieldId);
    this.fieldCurrent.id = field.id;
    this.fieldCurrent.name = field.name;
    this.sharedService.nextField(this.fieldCurrent);
  }

  insert() {
    if (this.form.invalid) return;
    
    this.productionService.insert(this.getObject(this.form.value))
      .subscribe(
        data => {
          this.snackBar.open('Production inserted with success!', 'Success');
          this.router.navigate(['/productions']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

}
