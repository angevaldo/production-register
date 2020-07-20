import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Farm, Field } from 'src/app/shared';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  private farm = new BehaviorSubject<Farm>(new Farm('?', null));
  sharedFarm = this.farm.asObservable();

  private field = new BehaviorSubject<Field>(new Field('?', null, null));
  sharedField = this.field.asObservable();

  constructor() { }

  nextFarm(farm: Farm) {
    this.farm.next(farm)
  }

  nextField(field: Field) {
    this.field.next(field)
  }
  
}
