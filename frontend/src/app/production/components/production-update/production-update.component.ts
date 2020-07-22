import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Production, ProductionService } from '../../../shared';

@Component({
  selector: 'app-production-update',
  templateUrl: './production-update.component.html',
  styleUrls: ['./production-update.component.scss']
})
export class ProductionUpdateComponent implements OnInit {

  form: FormGroup;
  production: Production = new Production(null, null);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router,
    private productionService: ProductionService,
  ) { }

  private getObject(data: any): Production {
    return new Production(
      data.value,
      this.production.fieldId,
      this.production.id
    );
  }

  ngOnInit(): void {
    this.findById();
    
    this.form = this.fb.group({
      value: ['', [Validators.required]]
    });
  }

  findById() {
    this.productionService.findById(this.route.snapshot.paramMap.get('productionId'))
      .subscribe(
        data => {
          this.production.value = data.value;
          this.production.fieldId = data.fieldId;
          this.production.id = data.id;
          this.form.get('value').setValue(data.value);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  update() {
    if (this.form.invalid) return;
    
    this.productionService.update(this.getObject(this.form.value))
      .subscribe(
        data => {
          this.snackBar.open('Production updated with success!', 'Success');
          this.router.navigate(['/productions']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  deleteById() {
    this.productionService.deleteById(this.production.id)
      .subscribe(
        data => {
          this.snackBar.open('Production deleted with success!', 'Success');
          this.router.navigate(['/productions']);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

}
