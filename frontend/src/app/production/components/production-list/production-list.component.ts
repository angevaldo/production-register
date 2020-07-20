import { Component, OnInit, ViewChild, ɵConsole } from '@angular/core';

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

  ngOnInit(): void {
    this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent);
    this.sharedService.sharedField.subscribe(fieldCurrent => this.fieldCurrent = fieldCurrent);

    this.findAllFarms();
    if (this.farmCurrent.id) {
      this.findFieldsByFarmId(this.farmCurrent.id);
    }

    this.updateDataTable();
  }

  findAllFarms() {
    this.farmService.findAll()
      .subscribe(
        data => { this.farms = data as Farm[]; },
        err => { this.snackBar.open(err.error.message, "Error"); }
      );
  }

  findFieldsByFarmId(farmId: string) {
    this.fieldService.findByFarmId(farmId)
      .subscribe(
        data => { this.fields = data as Field[]; },
        err => { this.snackBar.open(err.error.message, "Error"); }
      );
  }

  findByFarmId(farmId: string) {
    this.productionService.findByFarmId(farmId)
      .subscribe(
        data => {
          this.changeFarmCurrent(farmId);

          const productions = data as Production[];
          this.dataSource = new MatTableDataSource<Production>(productions);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        },
        err => {
          if (err.error.status == "404") {
            this.dataSource = new MatTableDataSource<Production>(<Production[]>[]);
          }
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  findByFieldId(fieldId: string) {
    this.productionService.findByFieldId(fieldId)
      .subscribe(
        data => {
          this.changeFieldCurrent(fieldId);

          const productions = data as Production[];
          this.dataSource = new MatTableDataSource<Production>(productions);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        },
        err => {
          if (err.error.status == "404") {
            this.dataSource = new MatTableDataSource<Production>(<Production[]>[]);
          }
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  changeFarmCurrent(farmId: string) {
    const farm: Farm = this.farms.find(s => s.id == farmId);
    this.farmCurrent.id = farm.id;
    this.farmCurrent.name = farm.name;
    this.sharedService.nextFarm(this.farmCurrent);

    this.fieldCurrent = new Field('?', null, farmId);
    this.sharedService.nextField(this.fieldCurrent);
    this.findFieldsByFarmId(farmId);
  }

  changeFieldCurrent(fieldId: string) {
    if (fieldId == null || this.fields == null) return;

    const field: Field = this.fields.find(s => s.id == fieldId);
    this.fieldCurrent.id = field.id;
    this.fieldCurrent.name = field.name;
    this.sharedService.nextField(this.fieldCurrent);
  }

  deleteById(productionId: string) {
    this.productionService.deleteById(productionId)
      .subscribe(
        data => {
          const msg: string = "Production deleted with success!";
          this.snackBar.open(msg, "Success");
          this.updateDataTable();
        },
        err => {
          this.snackBar.open(err.error.message, "Error");
        }
      );
  }

  updateDataTable() {
    if (this.fieldCurrent.id) {
      this.findByFieldId(this.fieldCurrent.id);
    } else if (this.farmCurrent.id) {
      this.findByFarmId(this.farmCurrent.id);
    }
  }

}