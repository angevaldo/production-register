import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Farm, FarmService } from '../../../shared';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit {

  farmId: string;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router,
    private farmService: FarmService
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
          let msg: string = "Error obtaining Farm";
          this.snackBar.open(msg, "Error");
          this.router.navigate(['/farms']);
        }
      );
  }

  update() {
    if (this.form.invalid) return;

    const farm: Farm = this.form.value;

    this.farmService.update(this.get(farm))
      .subscribe(
        data => {
          const msg: string = 'Farm updated with success!';
          this.snackBar.open(msg, "Success");
          this.router.navigate(['/farms']);
        },
        err => {
          let msg: string = 'Try again in minutes.';
          if (err.status == 400) {
            msg = err.error.errors.join(' ');
          }
          this.snackBar.open(msg, "Error");
        }
      );
  }

  get(data: any): Farm {
    return new Farm(
      data.name,
      this.farmId
    );
  }

}
