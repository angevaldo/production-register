import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { 
	FarmComponent,
	CreateComponent, 
	ListComponent ,
	UpdateComponent,
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
			path: 'new', 
			component: CreateComponent 
		  },
		  {
			path: ':farmId', 
			component: UpdateComponent 
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



