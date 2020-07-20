import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { 
	ProductionWrapperComponent,
	ProductionInsertComponent, 
	ProductionListComponent,
	ProductionUpdateComponent,
} from './components';

export const ProductionRoutes: Routes = [
	{
		path: 'productions',
		component: ProductionWrapperComponent,
		children: [
		  {
			path: '', 
			component: ProductionListComponent 
		  },
		  {
			path: 'new', 
			component: ProductionInsertComponent 
		  },
		  {
			path: ':productionId', 
			component: ProductionUpdateComponent 
		  }
		]
	}
];

@NgModule({
  imports: [
    RouterModule.forChild(ProductionRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class ProductionRoutingModule {
}