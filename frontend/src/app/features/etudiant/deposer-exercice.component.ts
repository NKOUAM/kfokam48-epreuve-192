import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ExerciceService } from '../../core/services/exercice.service';

@Component({
  selector: 'app-deposer-exercice',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="page">
      <a routerLink="/etudiant" class="retour">← Retour</a>
      <h1>Déposer mon exercice</h1>

      <form (ngSubmit)="deposer()" class="form">
        <label>ID de session
          <input type="number" [(ngModel)]="sessionId" name="sessionId" required />
        </label>
        <label>Mon identifiant
          <input type="number" [(ngModel)]="etudiantId" name="etudiantId" required />
        </label>
        <label>Lien de l'exercice
          <input [(ngModel)]="lien" name="lien" required placeholder="https://..." />
        </label>
        <button type="submit" [disabled]="chargement()">
          {{ chargement() ? 'Envoi...' : 'Déposer' }}
        </button>
      </form>

      @if (erreur()) { <div class="erreur">{{ erreur() }}</div> }
      @if (resultat()) {
        <div class="succes">
          Exercice déposé — statut : <strong>{{ resultat() }}</strong>
        </div>
      }
    </div>
  `,
  styles: [`
    .page { max-width: 500px; margin: 0 auto; padding: 2rem; }
    .retour { color: var(--vert); text-decoration: none; display: inline-block; margin-bottom: 1rem; }
    h1 { margin-top: 0; }
    .form { display: flex; flex-direction: column; gap: 1rem; }
    label { display: flex; flex-direction: column; gap: .4rem; color: var(--texte-secondaire); font-size: .9rem; }
    input { padding: .8rem; background: var(--noir-clair); border: 1px solid var(--gris);
      border-radius: 8px; color: var(--texte); font-size: 1rem; }
    input:focus { outline: none; border-color: var(--vert); }
    button { padding: .9rem; background: var(--vert); color: var(--noir); border: none;
      border-radius: 8px; font-weight: 600; cursor: pointer; }
    button:disabled { opacity: .5; }
    .erreur { margin-top: 1rem; padding: 1rem; background: rgba(239,68,68,.1);
      border: 1px solid var(--danger); border-radius: 8px; color: var(--danger); }
    .succes { margin-top: 1rem; padding: 1rem; background: rgba(34,197,94,.1);
      border: 1px solid var(--vert); border-radius: 8px; color: var(--vert); }
  `]
})
export class DeposerExerciceComponent {
  private service = inject(ExerciceService);
  sessionId = 1;
  etudiantId = 1;
  lien = '';
  chargement = signal(false);
  erreur = signal<string | null>(null);
  resultat = signal<string | null>(null);

  deposer() {
    this.chargement.set(true);
    this.erreur.set(null);
    this.resultat.set(null);
    this.service.deposer({ sessionId: this.sessionId, etudiantId: this.etudiantId, lien: this.lien })
      .subscribe({
        next: (r) => { this.resultat.set(r.statut); this.chargement.set(false); },
        error: (e) => { this.erreur.set(e.message); this.chargement.set(false); }
      });
  }
}
