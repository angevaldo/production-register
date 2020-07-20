import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Farm } from 'src/app/shared';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  private farm = new BehaviorSubject<Farm>(new Farm('?', null));
  sharedFarm = this.farm.asObservable();

  constructor() { }

  nextFarm(farm: Farm) {
    this.farm.next(farm)
  }
  
}
