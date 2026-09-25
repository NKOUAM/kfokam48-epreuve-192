import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PresenceService } from '../../core/services/presence.service';

@Component({
  selector: 'app-marquer-presence',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="page">
      <a routerLink="/etudiant" class="retour">← Retour</a>
      <h1>Marquer ma présence</h1>

      <form (ngSubmit)="marquer()" class="form">
        <label>Code de session
          <input [(ngModel)]="code" name="code" required maxlength="6" placeholder="XXXXXX" />
        </label>
        <label>Mon identifiant
          <input type="number" [(ngModel)]="etudiantId" name="etudiantId" required />
        </label>
        <button type="submit" [disabled]="chargement()">
          {{ chargement() ? 'Envoi...' : 'Marquer' }}
        </button>
      </form>

      @if (erreur()) { <div class="erreur">{{ erreur() }}</div> }
      @if (succes()) { <div class="succes">Présence enregistrée ✓</div> }
    </div>
  `,
  styles: [`
    .page { max-width: 500px; margin: 0 auto; padding: 2rem; }
    .retour { color: var(--vert); text-decoration: none; display: inline-block; margin-bottom: 1rem; }
    h1 { margin-top: 0; }
    .form { display: flex; flex-direction: column; gap: 1rem; }
    label { display: flex; flex-direction: column; gap: .4rem; color: var(--texte-secondaire); font-size: .9rem; }
    input { padding: .8rem; background: var(--noir-clair); border: 1px solid var(--gris);
      border-radius: 8px; color: var(--texte); font-size: 1rem; text-transform: uppercase; }
    input:focus { outline: none; border-color: var(--vert); }
    button { padding: .9rem; background: var(--vert); color: var(--noir); border: none;
      border-radius: 8px; font-weight: 600; font-size: 1rem; cursor: pointer; }
    button:disabled { opacity: .5; }
    .erreur { margin-top: 1rem; padding: 1rem; background: rgba(239,68,68,.1);
      border: 1px solid var(--danger); border-radius: 8px; color: var(--danger); }
    .succes { margin-top: 1rem; padding: 1rem; background: rgba(34,197,94,.1);
      border: 1px solid var(--vert); border-radius: 8px; color: var(--vert); }
  `]
})
export class MarquerPresenceComponent {
  private service = inject(PresenceService);
  code = '';
  etudiantId = 1;
  chargement = signal(false);
  erreur = signal<string | null>(null);
  succes = signal(false);

  marquer() {
    this.chargement.set(true);
    this.erreur.set(null);
    this.succes.set(false);
    this.service.marquer({ code: this.code.toUpperCase(), etudiantId: this.etudiantId }).subscribe({
      next: () => { this.succes.set(true); this.chargement.set(false); this.code = ''; },
      error: (e) => { this.erreur.set(e.message); this.chargement.set(false); }
    });
  }
}
