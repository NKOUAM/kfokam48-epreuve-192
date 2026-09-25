import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { RelectureService } from '../../core/services/relecture.service';

@Component({
  selector: 'app-faire-relecture',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="page">
      <a routerLink="/relecteur" class="retour">← Retour</a>
      <h1>Faire une relecture</h1>

      <form (ngSubmit)="rendre()" class="form">
        <label>ID de la relecture
          <input type="number" [(ngModel)]="relectureId" name="relectureId" required />
        </label>
        <label>Note (0 à 20)
          <input type="number" [(ngModel)]="note" name="note" required min="0" max="20" />
        </label>
        <label>Commentaire
          <textarea [(ngModel)]="commentaire" name="commentaire" rows="4"></textarea>
        </label>
        <button type="submit" [disabled]="chargement()">
          {{ chargement() ? 'Envoi...' : 'Envoyer' }}
        </button>
      </form>

      @if (erreur()) { <div class="erreur">{{ erreur() }}</div> }
      @if (succes()) { <div class="succes">Relecture enregistrée ✓</div> }
    </div>
  `,
  styles: [`
    .page { max-width: 500px; margin: 0 auto; padding: 2rem; }
    .retour { color: var(--vert); text-decoration: none; display: inline-block; margin-bottom: 1rem; }
    h1 { margin-top: 0; }
    .form { display: flex; flex-direction: column; gap: 1rem; }
    label { display: flex; flex-direction: column; gap: .4rem; color: var(--texte-secondaire); font-size: .9rem; }
    input, textarea { padding: .8rem; background: var(--noir-clair); border: 1px solid var(--gris);
      border-radius: 8px; color: var(--texte); font-size: 1rem; font-family: inherit; }
    input:focus, textarea:focus { outline: none; border-color: var(--vert); }
    button { padding: .9rem; background: var(--vert); color: var(--noir); border: none;
      border-radius: 8px; font-weight: 600; cursor: pointer; }
    button:disabled { opacity: .5; }
    .erreur { margin-top: 1rem; padding: 1rem; background: rgba(239,68,68,.1);
      border: 1px solid var(--danger); border-radius: 8px; color: var(--danger); }
    .succes { margin-top: 1rem; padding: 1rem; background: rgba(34,197,94,.1);
      border: 1px solid var(--vert); border-radius: 8px; color: var(--vert); }
  `]
})
export class FaireRelectureComponent {
  private service = inject(RelectureService);
  relectureId = 1;
  note = 10;
  commentaire = '';
  chargement = signal(false);
  erreur = signal<string | null>(null);
  succes = signal(false);

  rendre() {
    this.chargement.set(true);
    this.erreur.set(null);
    this.succes.set(false);
    this.service.rendre(this.relectureId, { note: this.note, commentaire: this.commentaire })
      .subscribe({
        next: () => { this.succes.set(true); this.chargement.set(false); },
        error: (e) => { this.erreur.set(e.message); this.chargement.set(false); }
      });
  }
}
