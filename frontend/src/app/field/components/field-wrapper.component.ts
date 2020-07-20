import { Component } from '@angular/core';

@Component ({
    template:`
        <h1 fxLayoutAlign="center">Fields</h1>
        <mat-divider></mat-divider><br>
        <router-outlet></router-outlet>
    `
})
export class FieldWrapperComponent {
}