import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment as env } from '../../../environments/environment';

import { Field } from '../models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FieldService {

  private readonly URI: string = 'fields';

  constructor(
    private http: HttpClient
  ) { }

  findByFarmId(farmId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "?farmId=" + farmId
    );
  }

  findById(fieldId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "/" + fieldId
    );
  }

  insert(field: Field): Observable<any> {
    return this.http.post(
      env.baseApiUrl + this.URI,
      field
    );
  }

  update(field: Field): Observable<any> {
    return this.http.put(
      env.baseApiUrl + this.URI + "/" + field.id,
      field
    );
  }

  deleteById(fieldId: string): Observable<any> {
    return this.http.delete(
      env.baseApiUrl + this.URI + "/" + fieldId
    );
  }

}
