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

  ngOnInit(): void {
    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);
    this.sharedService.sharedField.subscribe(fieldCurrent => this.fieldCurrent = fieldCurrent);

    this.findAllFarms();
    this.updateDataTable();

    this.form = this.fb.group({
      value: ['', [Validators.required]],
      farmId: [this.farmCurrent.id],
      fieldId: [this.fieldCurrent.id, [Validators.required]]
    });
  }

  changeFarmCurrent(farmId: string) {
    const farm: Farm = this.farms.find(s => s.id == farmId);
    this.farmCurrent.id = farm.id;
    this.farmCurrent.name = farm.name;
    this.sharedService.nextFarm(this.farmCurrent);

    this.fieldCurrent = new Field('?', null, farmId);
    this.sharedService.nextField(this.fieldCurrent);
    this.findFieldsByFarmId(farmId);
  }

  changeFieldCurrent(fieldId: string) {
    const field: Field = this.fields.find(s => s.id == fieldId);
    this.fieldCurrent.id = field.id;
    this.fieldCurrent.name = field.name;
    this.fieldCurrent.area = field.area;
    this.sharedService.nextField(this.fieldCurrent);
  }

  findAllFarms() {
    this.farmService.findAll()
      .subscribe(
        data => { this.farms = data as Farm[]; },
        err => { this.snackBar.open(err.error.message, "Error"); }
      );
  }

  findFieldsByFarmId(farmId: string) {
    this.fieldService.findByFarmId(farmId)
      .subscribe(
        data => { this.fields = data as Field[]; },
        err => { this.snackBar.open(err.error.message, "Error"); }
      );
  }

  insert() {
    if (this.form.invalid) return;
    
    this.productionService.insert(this.getObjectFromForm(this.form.value))
      .subscribe(
        data => {
          const msg: string = 'Production inserted with success!';
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/productions']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  getObjectFromForm(data: any): Production {
    return new Production(
      data.value,
      data.fieldId,
      null
    );
  }

  updateDataTable() {
    if (this.farmCurrent.id) {
      this.findFieldsByFarmId(this.farmCurrent.id);
    }
  }

}
