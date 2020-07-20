import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { 
	FarmWrapperComponent,
	FarmInsertComponent, 
	FarmListComponent,
	FarmUpdateComponent,
} from './components';

export const FarmRoutes: Routes = [
	{
		path: 'farms',
		component: FarmWrapperComponent,
		children: [
		  {
			path: '', 
			component: FarmListComponent 
		  },
		  {
			path: 'new', 
			component: FarmInsertComponent 
		  },
		  {
			path: ':farmId', 
			component: FarmUpdateComponent 
		  }
		]
	}
];

@NgModule({
  imports: [
    RouterModule.forChild(FarmRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class FarmRoutingModule {
}



