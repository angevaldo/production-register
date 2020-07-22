import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Farm, FarmService, SharedService } from '../../../shared';

@Component({
  selector: 'app-farm-insert',
  templateUrl: './farm-insert.component.html',
  styleUrls: ['./farm-insert.component.scss']
})
export class FarmInsertComponent implements OnInit {

  form: FormGroup;

  constructor(
  	private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private farmService: FarmService,
    private sharedService: SharedService
  ) { }

  private getObject(data: any): Farm {
    return new Farm(
      data.name,
      null
    );
  }

  ngOnInit(): void {
  	this.form = this.fb.group({
  		name: ['', [Validators.required, Validators.minLength(3)]]
  	});
  }

  insert() {
  	if (this.form.invalid) return;

    let farm: Farm = this.getObject(this.form.value);
    this.farmService.insert(farm)
      .subscribe(
        data => {
          this.snackBar.open('Farm inserted with success!', 'Success');
          this.router.navigate(['/farms']);
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

}
