import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RendreRelectureRequest } from '../models/relecture.model';

@Injectable({ providedIn: 'root' })
export class RelectureService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/relectures`;

  rendre(id: number, req: RendreRelectureRequest): Observable<void> {
    return this.http.post<void>(`${this.url}/${id}`, req);
  }
}
