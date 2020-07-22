import { Component, OnInit, ViewChild } from '@angular/core';

import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import { FarmService, ProductionService, Production, Farm, Field, FieldService } from '../../../shared';
import { SharedService } from '../../services';

@Component({
  selector: 'app-production-list',
  templateUrl: './production-list.component.html',
  styleUrls: ['./production-list.component.scss']
})
export class ProductionListComponent implements OnInit {

  dataSource: MatTableDataSource<Production>;
  columns: string[] = ['actions', 'value', 'id'];
  farms: Farm[];
  farmCurrent: Farm;
  fields: Field[];
  fieldCurrent: Field;

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(
    private productionService: ProductionService,
    private farmService: FarmService,
    private fieldService: FieldService,
    private sharedService: SharedService,
    private snackBar: MatSnackBar) { }

  private populateSelectFarms() {
    this.farmService.findAll()
      .subscribe(
        data => { this.farms = data as Farm[]; },
        err => { this.snackBar.open(err.error.message, 'Error'); }
      );
  }

  private populateSelecFields() {
    if (this.farmCurrent.id) {
      this.fieldService.findByFarmId(this.farmCurrent.id)
        .subscribe(
          data => { this.fields = data as Field[]; },
          err => { this.snackBar.open(err.error.message, 'Error'); }
        );
    }
  }

  private populateTableData() {
    if (this.fieldCurrent.id) {
      this.findByFieldId();
    } else if (this.farmCurrent.id) {
      this.findByFarmId();
    } else {
      this.fillTableData(<Production[]>[]);
    }
  }

  private fillTableData(productions: Production[]) {
    this.dataSource = new MatTableDataSource<Production>(productions);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(): void {
    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);
    this.sharedService.sharedField.subscribe(fieldCurrent => this.fieldCurrent = fieldCurrent);

    this.populateSelectFarms();
    this.populateSelecFields();
    this.populateTableData();
  }

  onChangeFarmCurrent(farmId: string) {
    if (farmId == null || this.farms == null) return;

    const farm: Farm = this.farms.find(s => s.id == farmId);
    this.farmCurrent.id = farm.id;
    this.farmCurrent.name = farm.name;
    this.sharedService.nextFarm(this.farmCurrent);

    this.fieldCurrent = new Field('?', null, farmId);
    this.sharedService.nextField(this.fieldCurrent);

    this.populateSelecFields();

    this.findByFarmId();
  } 

  onChangeFieldCurrent(fieldId: string) {
    if (fieldId == null || this.fields == null) return;

    const field: Field = this.fields.find(s => s.id == fieldId);
    this.fieldCurrent.id = field.id;
    this.fieldCurrent.name = field.name;
    this.sharedService.nextField(this.fieldCurrent);

    this.findByFieldId();
  }

  findByFarmId() {
    this.productionService.findByFarmId(this.farmCurrent.id)
      .subscribe(
        data => {
          this.fillTableData(data as Production[]);
        },
        err => {
          if (err.error.status == '404') {
            this.fillTableData(<Production[]>[]);
          }
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  findByFieldId() {
    this.productionService.findByFieldId(this.fieldCurrent.id)
      .subscribe(
        data => {
          this.fillTableData(data as Production[]);
        },
        err => {
          if (err.error.status == '404') {
            this.fillTableData(<Production[]>[]);
          }
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

  deleteById(productionId: string) {
    this.productionService.deleteById(productionId)
      .subscribe(
        data => {
          this.snackBar.open('Production deleted with success!', 'Success');
          this.populateTableData();
        },
        err => {
          this.snackBar.open(err.error.message, 'Error');
        }
      );
  }

}