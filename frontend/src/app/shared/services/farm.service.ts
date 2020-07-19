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

  create(farm: Farm): Observable<any> {
    return this.http.post(
      env.baseApiUrl + this.URI,
      farm
    );
  }

}
