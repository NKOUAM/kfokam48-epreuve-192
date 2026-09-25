import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DeposerExerciceRequest, ExerciceResponse, RelecturePubliqueResponse } from '../models/exercice.model';

@Injectable({ providedIn: 'root' })
export class ExerciceService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/exercices`;

  deposer(req: DeposerExerciceRequest): Observable<ExerciceResponse> {
    return this.http.post<ExerciceResponse>(this.url, req);
  }

  consulterRelecture(id: number): Observable<RelecturePubliqueResponse> {
    return this.http.get<RelecturePubliqueResponse>(`${this.url}/${id}/relecture`);
  }
}
