import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Farm, FarmService, ProductionService } from '../../../shared';

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
    private productionService: ProductionService
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
          this.form.get('name').setValue(data.name);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  update() {
    if (this.form.invalid) return;

    this.farmService.update(this.getObject(this.form.value))
      .subscribe(
        data => {
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
