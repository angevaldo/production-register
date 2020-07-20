import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { 
	FieldWrapperComponent,
	FieldInsertComponent, 
	FieldListComponent,
	FieldUpdateComponent,
} from './components';

export const FieldRoutes: Routes = [
	{
		path: 'fields',
		component: FieldWrapperComponent,
		children: [
		  {
			path: '', 
			component: FieldListComponent 
		  },
		  {
			path: 'new', 
			component: FieldInsertComponent 
		  },
		  {
			path: ':fieldId', 
			component: FieldUpdateComponent 
		  }
		]
	}
];

@NgModule({
  imports: [
    RouterModule.forChild(FieldRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class FieldRoutingModule {
}