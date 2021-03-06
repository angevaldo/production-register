import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment as env } from '../../../environments/environment';

import { Production } from '../models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductionService {

  private readonly URI: string = 'productions';

  constructor(
    private http: HttpClient
  ) { }

  findByFarmId(farmId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "/findByFarmId?farmId=" + farmId
    );
  }

  findByFieldId(fieldId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "/findByFieldId?fieldId=" + fieldId
    );
  }

  productivityByFarmId(farmId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "/productivityByFarmId?farmId=" + farmId
    );
  }

  productivityByFieldId(fieldId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "/productivityByFieldId?fieldId=" + fieldId
    );
  }

  findById(productionId: string): Observable<any> {
    return this.http.get(
      env.baseApiUrl + this.URI + "/" + productionId
    );
  }

  insert(production: Production): Observable<any> {
    return this.http.post(
      env.baseApiUrl + this.URI,
      production
    );
  }

  update(production: Production): Observable<any> {
    return this.http.put(
      env.baseApiUrl + this.URI + "/" + production.id,
      production
    );
  }

  deleteById(productionId: string): Observable<any> {
    return this.http.delete(
      env.baseApiUrl + this.URI + "/" + productionId
    );
  }

}
