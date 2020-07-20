import { Component, OnInit, ViewChild, Inject } from '@angular/core';

import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import { FieldService, Field, Farm } from '../../../shared';

@Component({
  selector: 'app-field-list',
  templateUrl: './field-list.component.html',
  styleUrls: ['./field-list.component.scss']
})
export class FieldListComponent implements OnInit {

  dataSource: MatTableDataSource<Field>;
  columns: string[] = ['actions', 'name', 'area', 'id'];
  farms: Farm[] = [
    { id: '5f14f2aa1b6eb7748b946ea9', name: 'Farm 01' },
    { id: '5f15144167ac8b06195e51d0', name: 'steest' }
  ];
  farmId: string;
  farmName: string;

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(
    private fieldService: FieldService,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.findByFarmId("5f14f2aa1b6eb7748b946ea9");
  }

  findByFarmId(farmId: string) {
    this.fieldService.findByFarmId(farmId)
      .subscribe(
        data => {
          this.farmId = farmId;
          this.farmName = this.farms.find(s => s.id == farmId).name;
          
          const fields = data as Field[];
          this.dataSource = new MatTableDataSource<Field>(fields);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  deleteById(fieldId: string) {
    this.fieldService.deleteById(fieldId)
      .subscribe(
        data => {
          const msg: string = "Field deleted with success!";
          this.snackBar.open(msg, "Success");
          this.findByFarmId(this.farmId);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

}