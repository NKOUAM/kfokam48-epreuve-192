import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      const body = err.error;
      const message = body?.message ?? body?.code ?? `Erreur ${err.status}`;
      console.error(`[API ${err.status}]`, message);
      return throwError(() => ({ status: err.status, message, code: body?.code }));
    })
  );
};
