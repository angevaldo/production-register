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

  ngOnInit(): void {
    this.farmId = this.route.snapshot.paramMap.get('farmId');
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]]
    });

    this.findById(this.farmId);
  }

  findById(id: string) {
    this.farmService.findById(id)
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

    this.farmService.update(this.getObjectFromForm(this.form.value))
      .subscribe(
        data => {
          const msg: string = 'Farm updated with success!';
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/farms']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  deleteById() {
    this.farmService.deleteById(this.farmId)
      .subscribe(
        data => {
          const msg: string = "Farm deleted with success!";
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/farms']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  getProductivity() {
    this.productionService.productivityByFarmId(this.farmId)
      .subscribe(
        data => { this.snackBar.open(Number(data).toLocaleString('en', {maximumFractionDigits:2}), "Productivity"); },
        err => { this.snackBar.open(err.error.message, "Error"); }
      );
  }

  getObjectFromForm(data: any): Farm {
    return new Farm(
      data.name,
      this.farmId
    );
  }

}
