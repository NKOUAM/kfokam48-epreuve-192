import { Routes } from '@angular/router';
import { AccueilComponent } from './features/accueil/accueil.component';
import { OuvrirSessionComponent } from './features/formateur/ouvrir-session.component';
import { TableauComponent } from './features/formateur/tableau.component';
import { MarquerPresenceComponent } from './features/etudiant/marquer-presence.component';
import { DeposerExerciceComponent } from './features/etudiant/deposer-exercice.component';
import { FaireRelectureComponent } from './features/relecteur/faire-relecture.component';

export const routes: Routes = [
  { path: '', component: AccueilComponent },
  { path: 'formateur', component: OuvrirSessionComponent },
  { path: 'formateur/tableau', component: TableauComponent },
  { path: 'etudiant', component: MarquerPresenceComponent },
  { path: 'etudiant/deposer', component: DeposerExerciceComponent },
  { path: 'relecteur', component: FaireRelectureComponent },
  { path: '**', redirectTo: '' }
];
