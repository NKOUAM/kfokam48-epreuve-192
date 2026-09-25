import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MarquerPresenceRequest, PresenceResponse } from '../models/presence.model';

@Injectable({ providedIn: 'root' })
export class PresenceService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/presences`;

  marquer(req: MarquerPresenceRequest): Observable<PresenceResponse> {
    return this.http.post<PresenceResponse>(this.url, req);
  }

  ajouterParFormateur(sessionId: number, etudiantId: number): Observable<PresenceResponse> {
    return this.http.post<PresenceResponse>(`${this.url}/formateur`, { sessionId, etudiantId });
  }
}
