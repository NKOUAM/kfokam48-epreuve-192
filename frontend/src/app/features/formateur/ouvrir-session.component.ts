import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SessionService } from '../../core/services/session.service';
import { SessionResponse } from '../../core/models/session.model';

@Component({
  selector: 'app-ouvrir-session',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="page">
      <a routerLink="/formateur" class="retour">← Retour</a>
      <h1>Ouvrir une session</h1>

      <form (ngSubmit)="ouvrir()" class="form">
        <label>Titre
          <input [(ngModel)]="titre" name="titre" required placeholder="Cours Java 101" />
        </label>
        <label>Promotion
          <input type="number" [(ngModel)]="promotionId" name="promotionId" required />
        </label>
        <button type="submit" [disabled]="chargement()">
          {{ chargement() ? 'Ouverture...' : 'Ouvrir' }}
        </button>
      </form>

      @if (erreur()) { <div class="erreur">{{ erreur() }}</div> }

      @if (session()) {
        <div class="resultat">
          <h2>Session ouverte</h2>
          <div class="code">{{ session()!.code }}</div>
          <p>Expire à {{ session()!.expirationAt.substring(11, 16) }}</p>
        </div>
      }
    </div>
  `,
  styles: [`
    .page { max-width: 600px; margin: 0 auto; padding: 2rem; }
    .retour { color: var(--vert); text-decoration: none; display: inline-block; margin-bottom: 1rem; }
    h1 { margin-top: 0; }
    .form { display: flex; flex-direction: column; gap: 1rem; }
    label { display: flex; flex-direction: column; gap: .4rem; color: var(--texte-secondaire); font-size: .9rem; }
    input { padding: .8rem; background: var(--noir-clair); border: 1px solid var(--gris);
      border-radius: 8px; color: var(--texte); font-size: 1rem; }
    input:focus { outline: none; border-color: var(--vert); }
    button { padding: .9rem; background: var(--vert); color: var(--noir); border: none;
      border-radius: 8px; font-weight: 600; font-size: 1rem; cursor: pointer; transition: background .2s; }
    button:hover:not(:disabled) { background: var(--vert-fonce); }
    button:disabled { opacity: .5; cursor: not-allowed; }
    .erreur { margin-top: 1rem; padding: 1rem; background: rgba(239,68,68,.1);
      border: 1px solid var(--danger); border-radius: 8px; color: var(--danger); }
    .resultat { margin-top: 2rem; padding: 1.5rem; background: var(--noir-clair);
      border: 1px solid var(--vert); border-radius: 12px; text-align: center; }
    .code { font-size: 3rem; font-weight: 700; color: var(--vert); letter-spacing: 6px; margin: 1rem 0; }
  `]
})
export class OuvrirSessionComponent {
  private sessionService = inject(SessionService);
  titre = '';
  promotionId = 1;
  chargement = signal(false);
  erreur = signal<string | null>(null);
  session = signal<SessionResponse | null>(null);

  ouvrir() {
    this.chargement.set(true);
    this.erreur.set(null);
    this.sessionService.ouvrir({ titre: this.titre, promotionId: this.promotionId }).subscribe({
      next: (s) => { this.session.set(s); this.chargement.set(false); },
      error: (e) => { this.erreur.set(e.message); this.chargement.set(false); }
    });
  }
}
