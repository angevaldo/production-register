import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import { FarmService, FieldService, Field, Farm } from '../../../shared';
import { SharedService } from '../../../shared';

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

  private populateSelectFarms() {
    this.farmService.findAll()
      .subscribe(
        data => { this.farms = data as Farm[]; },
        err => { this.snackBar.open(err.error.message, 'Error'); }
      );
  }

  private populateTableData() {
    if (this.farmCurrent.id) {
      this.findByFarmId();
    } else {
      this.fillTableData(<Field[]>[]);
    }
  }

  private fillTableData(fields: Field[]) {
    this.dataSource = new MatTableDataSource<Field>(fields);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(): void {
    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);

    this.populateSelectFarms();
    this.populateTableData();
  }

  onChangeFarmCurrent(farmId: string) {
    if (farmId == null || this.farms == null) return;

    const farm: Farm = this.farms.find(s => s.id == farmId);
    this.farmCurrent.id = farm.id;
    this.farmCurrent.name = farm.name;
    this.sharedService.nextFarm(this.farmCurrent);

    this.findByFarmId();
  }

  findByFarmId() {
    this.fieldService.findByFarmId(this.farmCurrent.id)
      .subscribe(
        data => {
          this.fillTableData(data as Field[]);
        },
        err => {
          if (err.error.status == '404') {
            this.fillTableData(<Field[]>[]);
          }
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  deleteById(fieldId: string) {
    this.fieldService.deleteById(fieldId)
      .subscribe(
        data => {
          this.sharedService.nextField(new Field('?', null, null));
          this.snackBar.open('Field deleted with success!', 'Success');
          this.populateTableData();
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

}