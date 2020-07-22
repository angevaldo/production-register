import { Component } from '@angular/core';
import { Farm } from 'src/app/shared';
import { SharedService } from "../../shared";

@Component({
    template: `
        <h1 fxLayoutAlign="center">
            Fields of {{ farmCurrent.name }}
        </h1>
        <mat-divider></mat-divider><br>
        <router-outlet></router-outlet>
    `
})
export class FieldWrapperComponent {

    farmCurrent: Farm;

    constructor(private sharedService: SharedService) { }

    ngOnInit() {
        this.sharedService.sharedFarm.subscribe(farmCurrent => this.farmCurrent = farmCurrent)
    }

}