import { Component, Input } from '@angular/core';
import { Farm } from 'src/app/shared';
import { SharedService } from "../services";

@Component({
    template: `
        <h1 fxLayoutAlign="center">
            Productions of {{ farmCurrent.name }}
        </h1>
        <mat-divider></mat-divider><br>
        <router-outlet></router-outlet>
    `
})
export class ProductionWrapperComponent {

    farmCurrent: Farm;

    constructor(private sharedService: SharedService) { }

    ngOnInit() {
        this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent)
    }

}