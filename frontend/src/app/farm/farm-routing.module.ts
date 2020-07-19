import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { 
	FarmComponent,
	CreateComponent, 
	ListComponent 
} from './components';

export const FarmRoutes: Routes = [
	{
		path: 'farms',
		component: FarmComponent,
		children: [
		  {
			path: '', 
			component: ListComponent 
		  },
		  {
			path: 'create', 
			component: CreateComponent 
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



