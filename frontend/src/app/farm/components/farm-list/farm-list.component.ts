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

  private updateDataTable(farms: Farm[]) {
    this.dataSource = new MatTableDataSource<Farm>(farms);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.farmService.findAll()
      .subscribe(
        data => {
          this.updateDataTable(data as Farm[]);
        },
        err => {
          if (err.error.status == "404") {
            this.updateDataTable(<Farm[]>[]);
          }
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  deleteById(farmId: string) {
    this.farmService.deleteById(farmId)
      .subscribe(
        data => {
          this.snackBar.open("Farm deleted with success!", "Success");
          this.findAll();
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

}