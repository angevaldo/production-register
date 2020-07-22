import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Farm, FarmService, ProductionService, SharedService } from '../../../shared';

@Component({
  selector: 'app-farm-update',
  templateUrl: './farm-update.component.html',
  styleUrls: ['./farm-update.component.scss']
})
export class FarmUpdateComponent implements OnInit {

  farmId: string;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router,
    private farmService: FarmService,
    private productionService: ProductionService,
    private sharedService: SharedService
  ) { }

  private getObject(data: any): Farm {
    return new Farm(
      data.name,
      this.farmId
    );
  }

  ngOnInit(): void {
    this.farmId = this.route.snapshot.paramMap.get('farmId');

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]]
    });

    this.findById();
  }

  findById() {
    this.farmService.findById(this.farmId)
      .subscribe(
        data => {
          this.sharedService.nextFarm(new Farm(data.name, data.id));
          this.form.get('name').setValue(data.name);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  update() {
    if (this.form.invalid) return;

    let farm: Farm = this.getObject(this.form.value);
    this.farmService.update(farm)
      .subscribe(
        data => {
          this.sharedService.nextFarm(farm);
          this.snackBar.open('Farm updated with success!', 'Success');
          this.router.navigate(['/farms']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  deleteById() {
    this.farmService.deleteById(this.farmId)
      .subscribe(
        data => {
          this.snackBar.open('Farm deleted with success!', 'Success');
          this.router.navigate(['/farms']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  getProductivity() {
    this.productionService.productivityByFarmId(this.farmId)
      .subscribe(
        data => { this.snackBar.open(Number(data).toLocaleString('en', {maximumFractionDigits:2}), 'Productivity'); },
        err => { this.snackBar.open(err.error.message, 'Error'); }
      );
  }

}
