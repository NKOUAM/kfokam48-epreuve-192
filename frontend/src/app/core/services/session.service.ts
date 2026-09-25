import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OuvrirSessionRequest, SessionResponse } from '../models/session.model';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/sessions`;

  ouvrir(req: OuvrirSessionRequest): Observable<SessionResponse> {
    return this.http.post<SessionResponse>(this.url, req);
  }

  cloturer(id: number): Observable<void> {
    return this.http.post<void>(`${this.url}/${id}/cloture`, {});
  }
}
