import { Component, OnInit, ViewChild } from '@angular/core';

import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import { FarmService, FieldService, Field, Farm } from '../../../shared';
import { SharedService } from '../../services';

@Component({
  selector: 'app-field-list',
  templateUrl: './field-list.component.html',
  styleUrls: ['./field-list.component.scss']
})
export class FieldListComponent implements OnInit {

  dataSource: MatTableDataSource<Field>;
  columns: string[] = ['actions', 'name', 'area', 'id'];
  farms: Farm[];
  farmCurrent: Farm;

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(
    private fieldService: FieldService,
    private farmService: FarmService,
    private sharedService: SharedService,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.findAllFarms();

    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);
    if (this.farmCurrent && this.farmCurrent.id) {
      this.findByFarmId(this.farmCurrent.id);
    }
  }

  findAllFarms() {
    this.farmService.findAll()
      .subscribe(
        data => { this.farms = data as Farm[]; },
        err => { this.snackBar.open(err.error.message, "Error"); }
      );
  }

  findByFarmId(farmId: string) {
    this.fieldService.findByFarmId(farmId)
      .subscribe(
        data => {
          this.changeFarmCurrent(farmId);
          
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

  changeFarmCurrent(farmId: string) {
    const farm: Farm = this.farms.find(s => s.id == farmId);
    this.farmCurrent.id = farm.id;
    this.farmCurrent.name = farm.name;
    this.sharedService.nextFarm(this.farmCurrent);
  }

  deleteById(fieldId: string) {
    this.fieldService.deleteById(fieldId)
      .subscribe(
        data => {
          const msg: string = "Field deleted with success!";
          this.snackBar.open(msg, "Success");
          this.findByFarmId(this.farmCurrent.id);
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

}