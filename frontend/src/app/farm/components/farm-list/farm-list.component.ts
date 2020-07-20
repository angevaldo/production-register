import { Component, OnInit, ViewChild, Inject } from '@angular/core';

import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import { FarmService, Farm } from '../../../shared';

@Component({
  selector: 'app-farm-list',
  templateUrl: './farm-list.component.html',
  styleUrls: ['./farm-list.component.scss']
})
export class FarmListComponent implements OnInit {

  dataSource: MatTableDataSource<Farm>;
  columns: string[] = ['actions', 'name', 'id'];

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(
    private farmService: FarmService,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.farmService.findAll()
      .subscribe(
        data => {
          const farms = data as Farm[];
          this.dataSource = new MatTableDataSource<Farm>(farms);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        },
        err => {
          const msg: string = "Error obtaining farms.";
          this.snackBar.open(msg, "Error");
        }
      );
  }

  deleteById(farmId: string) {
    this.farmService.deleteById(farmId)
      .subscribe(
        data => {
          const msg: string = "Farm deleted with success!";
          this.snackBar.open(msg, "Success");
          this.findAll();
        },
        err => {
          let msg: string = "Try again later.";
          if (err.status == 400) {
            msg = err.error.errors.join(' ');
          }
          this.snackBar.open(msg, "Error");
        }
      );
  }

}