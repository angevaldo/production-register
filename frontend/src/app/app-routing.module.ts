import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductionWrapperComponent, ProductionInsertComponent } from './production/components';


const routes: Routes = [
  {
    path: '',
    component: ProductionInsertComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }