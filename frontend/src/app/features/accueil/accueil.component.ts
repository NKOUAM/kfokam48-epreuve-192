import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-accueil',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="accueil">
      <h1>KFOKAM48 <span class="accent">Évaluation</span></h1>
      <p class="sous-titre">Présence · Dépôt d'exercices · Relecture</p>

      <div class="cartes">
        <a routerLink="/formateur" class="carte">
          <span class="emoji">◆</span>
          <h2>Formateur</h2>
          <p>Ouvrir une session, voir le tableau</p>
        </a>
        <a routerLink="/etudiant" class="carte">
          <span class="emoji">◇</span>
          <h2>Étudiant</h2>
          <p>Marquer ma présence, déposer mon exercice</p>
        </a>
        <a routerLink="/relecteur" class="carte">
          <span class="emoji">◈</span>
          <h2>Relecteur</h2>
          <p>Faire une relecture</p>
        </a>
      </div>
    </div>
  `,
  styles: [`
    .accueil { min-height: 100vh; display: flex; flex-direction: column;
      align-items: center; justify-content: center; padding: 2rem; gap: 2rem; }
    h1 { font-size: 3rem; margin: 0; letter-spacing: -1px; }
    .accent { color: var(--vert); }
    .sous-titre { color: var(--texte-secondaire); font-size: 1.1rem; margin: 0; }
    .cartes { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 1.5rem; max-width: 900px; width: 100%; }
    .carte { display: block; padding: 2rem 1.5rem; background: var(--noir-clair);
      border: 1px solid var(--gris); border-radius: 12px; text-decoration: none;
      color: var(--texte); transition: all .2s; }
    .carte:hover { border-color: var(--vert); transform: translateY(-4px); }
    .carte .emoji { font-size: 2rem; color: var(--vert); display: block; margin-bottom: .5rem; }
    .carte h2 { margin: 0 0 .5rem; font-size: 1.3rem; }
    .carte p { margin: 0; color: var(--texte-secondaire); font-size: .9rem; }
  `]
})
export class AccueilComponent {}
