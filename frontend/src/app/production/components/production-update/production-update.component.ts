import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Production, ProductionService, Farm } from '../../../shared';
import { SharedService } from '../../services';

@Component({
  selector: 'app-production-update',
  templateUrl: './production-update.component.html',
  styleUrls: ['./production-update.component.scss']
})
export class ProductionUpdateComponent implements OnInit {

  form: FormGroup;
  productionId: string;
  farmCurrent: Farm;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router,
    private productionService: ProductionService,
    private sharedService: SharedService
  ) { }

  ngOnInit(): void {
    this.productionId = this.route.snapshot.paramMap.get('productionId');
    this.form = this.fb.group({
      value: ['', [Validators.required]]
    });

    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);
    this.findById(this.productionId);
  }

  findById(id: string) {
    this.productionService.findById(id)
      .subscribe(
        data => {
          this.form.get('value').setValue(data.value);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  update() {
    if (this.form.invalid) return;

    console.log(this.farmCurrent.id)
    const production: Production = this.form.value;

    this.productionService.update(this.get(production))
      .subscribe(
        data => {
          const msg: string = 'Production updated with success!';
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/productions']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  deleteById() {
    this.productionService.deleteById(this.productionId)
      .subscribe(
        data => {
          const msg: string = "Production deleted with success!";
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/productions']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  get(data: any): Production {
    return new Production(
      data.value,
      this.farmCurrent.id,
      this.productionId
    );
  }

}
