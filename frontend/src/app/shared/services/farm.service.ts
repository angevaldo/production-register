import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment as env } from '../../../environments/environment';

import { Farm } from '../models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FarmService {

  private readonly URI: string = 'farms';

  constructor(
    private http: HttpClient
  ) { }

  findAll(): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI
    );
  }

  findById(farmId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "/" + farmId
    );
  }

  insert(farm: Farm): Observable<any> {
    return this.http.post(
      env.baseApiUrl + this.URI,
      farm
    );
  }

  update(farm: Farm): Observable<any> {
    return this.http.put(
      env.baseApiUrl + this.URI + "/" + farm.id,
      farm
    );
  }

  deleteById(farmId: string): Observable<any> {
    return this.http.delete(
      env.baseApiUrl + this.URI + "/" + farmId
    );
  }

}
