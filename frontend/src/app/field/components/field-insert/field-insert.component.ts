import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Field, FieldService, Farm, FarmService } from '../../../shared';
import { SharedService } from '../../../shared';

@Component({
  selector: 'app-field-insert',
  templateUrl: './field-insert.component.html',
  styleUrls: ['./field-insert.component.scss']
})
export class FieldInsertComponent implements OnInit {

  form: FormGroup;
  farms: Farm[];
  farmCurrent: Farm;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private fieldService: FieldService,
    private farmService: FarmService,
    private sharedService: SharedService
  ) { }

  private populateSelecFields() {
    this.farmService.findAll()
      .subscribe(
        data => { this.farms = data as Farm[]; },
        err => { this.snackBar.open(err.error.message, 'Error'); }
      );
  }

  private getObject(data: any): Field {
    return new Field(
      data.name,
      data.area,
      data.farmId,
      null
    );
  }

  ngOnInit(): void {
    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      area: ['', [Validators.required]],
      farmId: [this.farmCurrent.id, [Validators.required]]
    });
    
    this.populateSelecFields();
  }

  onChangeFarmCurrent(farmId: string) {
    const farm: Farm = this.farms.find(s => s.id == farmId);
    this.farmCurrent.id = farm.id;
    this.farmCurrent.name = farm.name;

    this.sharedService.nextFarm(this.farmCurrent);

    this.populateSelecFields();
  }

  insert() {
    if (this.form.invalid) return;

    let field: Field = this.getObject(this.form.value);
    this.fieldService.insert(field)
      .subscribe(
        data => {
          this.snackBar.open('Field inserted with success!', 'Success');
          this.router.navigate(['/fields']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

}
