import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TableauLigne } from '../models/tableau.model';

@Injectable({ providedIn: 'root' })
export class TableauService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/tableau`;

  charger(promotionId: number): Observable<TableauLigne[]> {
    return this.http.get<TableauLigne[]>(this.url, { params: { promotionId } });
  }
}
