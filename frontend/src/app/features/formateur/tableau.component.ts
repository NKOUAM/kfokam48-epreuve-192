import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TableauService } from '../../core/services/tableau.service';
import { TableauLigne } from '../../core/models/tableau.model';

@Component({
  selector: 'app-tableau',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="page">
      <a routerLink="/formateur" class="retour">← Retour</a>
      <h1>Tableau de bord</h1>

      <div class="filtre">
        <input type="number" [(ngModel)]="promotionId" placeholder="ID de promotion" />
        <button (click)="charger()" [disabled]="chargement()">Charger</button>
      </div>

      @if (erreur()) { <div class="erreur">{{ erreur() }}</div> }

      @if (lignes().length > 0) {
        <table>
          <thead>
            <tr>
              <th>Étudiant</th><th>Présences</th><th>Dépôts</th>
              <th>Moyenne</th><th>Relectures en attente</th>
            </tr>
          </thead>
          <tbody>
            @for (l of lignes(); track l.etudiantId) {
              <tr>
                <td>{{ l.nom }}</td>
                <td>{{ l.presences }}</td>
                <td>{{ l.exercicesDeposes }}</td>
                <td>{{ l.moyenne !== null ? l.moyenne.toFixed(2) : '—' }}</td>
                <td>{{ l.relecturesEnAttente }}</td>
              </tr>
            }
          </tbody>
        </table>
      }
    </div>
  `,
  styles: [`
    .page { max-width: 900px; margin: 0 auto; padding: 2rem; }
    .retour { color: var(--vert); text-decoration: none; display: inline-block; margin-bottom: 1rem; }
    h1 { margin-top: 0; }
    .filtre { display: flex; gap: 1rem; margin-bottom: 1.5rem; }
    .filtre input { flex: 1; padding: .8rem; background: var(--noir-clair);
      border: 1px solid var(--gris); border-radius: 8px; color: var(--texte); }
    .filtre button { padding: .8rem 1.5rem; background: var(--vert); color: var(--noir);
      border: none; border-radius: 8px; font-weight: 600; cursor: pointer; }
    table { width: 100%; border-collapse: collapse; background: var(--noir-clair);
      border-radius: 12px; overflow: hidden; }
    th, td { padding: 1rem; text-align: left; border-bottom: 1px solid var(--gris); }
    th { background: var(--noir); color: var(--vert); font-weight: 600; font-size: .85rem;
      text-transform: uppercase; letter-spacing: .5px; }
    tr:last-child td { border-bottom: none; }
    .erreur { padding: 1rem; background: rgba(239,68,68,.1); border: 1px solid var(--danger);
      border-radius: 8px; color: var(--danger); margin-bottom: 1rem; }
  `]
})
export class TableauComponent implements OnInit {
  private service = inject(TableauService);
  promotionId = 1;
  chargement = signal(false);
  erreur = signal<string | null>(null);
  lignes = signal<TableauLigne[]>([]);

  ngOnInit() { this.charger(); }

  charger() {
    this.chargement.set(true);
    this.erreur.set(null);
    this.service.charger(this.promotionId).subscribe({
      next: (l) => { this.lignes.set(l); this.chargement.set(false); },
      error: (e) => { this.erreur.set(e.message); this.chargement.set(false); }
    });
  }
}
