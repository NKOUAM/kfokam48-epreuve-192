# Cahier des charges — Plateforme de présence et de relecture KFOKAM48

**Auteur :** NKOUAM (matricule 192)
**Version :** 1
**Frontend choisi :** Angular, parce que le framework impose une architecture MVC claire (services, composants, guards) qui correspond exactement à la contrainte F3 du sujet (couche API dédiée, pas de fetch dispersé).

---

## 1. Contexte et objectif

La direction de la formation KFOKAM48 gère aujourd'hui les présences et la relecture des exercices de manière manuelle. Elle souhaite une application web qui :

1. permet à un formateur d'ouvrir une session et de générer un code de présence ;
2. permet à un étudiant de marquer sa présence avec ce code ;
3. permet à un étudiant de déposer le lien de son exercice ;
4. assigne automatiquement un relecteur à chaque exercice et permet à ce relecteur de noter et commenter ;
5. offre au formateur un tableau de bord consolidé par promotion.

**Objectif :** outiller la direction avec un outil traçable, fiable, et utilisable en mobilité par les étudiants.

---

## 2. Acteurs et rôles

| Acteur | Ce qu'il peut faire |
|---|---|
| **Formateur** | Ouvrir une session · consulter le code de présence et sa date d'expiration · ajouter manuellement une présence (traçée) · clôturer une session · consulter le tableau de bord d'une promotion |
| **Étudiant** | Marquer sa présence avec un code · déposer (ou remplacer) le lien de son exercice · consulter la note et le commentaire reçus (sans voir le nom du relecteur) |
| **Relecteur** (rôle porté par un étudiant) | Consulter la liste de ses relectures en attente · noter (0–20, entier) et commenter un exercice · corriger sa note tant que la session n'est pas clôturée |
| **Système** | Génère le code de présence · assigne le relecteur au hasard parmi les présents · calcule la moyenne des notes · fait respecter les règles de gestion |

---

## 3. Périmètre

**Inclus :**
- Gestion des promotions, étudiants, formateurs (données de démo).
- Ouverture / clôture de sessions.
- Marquage de présence via code, avec expiration et anti-fraude.
- Dépôt de lien d'exercice, remplacement tant que la relecture n'a pas commencé.
- Assignation aléatoire d'un relecteur, dépôt d'une note et d'un commentaire.
- Tableau de bord formateur par promotion.

**Exclu (explicitement) :**
- Authentification par mot de passe (Q1 : l'étudiant choisit son nom dans une liste).
- Envoi d'e-mails, de SMS ou de notifications push.
- Import de masse des étudiants (CSV, etc.).
- Gestion multi-promotions simultanée dans une même session.
- Rendu visuel soigné / CSS avancé (non noté par le sujet).
- Détection de plagiat.

---

## 4. Exigences fonctionnelles

| Réf | Exigence | Critère d'acceptation | Priorité |
|---|---|---|---|
| EF1 | Le formateur ouvre une session de cours | `POST /api/sessions` avec un titre et un promotionId valides renvoie `201` et le code, ouvertureAt, expirationAt | Must |
| EF2 | Le code de présence expire au bout de 15 minutes | Un code saisi après `expirationAt` renvoie `410 CODE_EXPIRE` | Must |
| EF3 | L'étudiant marque sa présence avec un code valide | `POST /api/presences` avec un code valide renvoie `201` et la présence apparaît dans le tableau du formateur | Must |
| EF4 | Un étudiant ne peut marquer sa présence qu'une fois par session | Un second `POST /api/presences` pour la même (session, étudiant) renvoie `409 DEJA_PRESENT` | Must |
| EF5 | L'étudiant dépose le lien de son exercice | `POST /api/exercices` avec un lien valide renvoie `201 { id, statut: "DEPOSE" }` | Must |
| EF6 | Un étudiant ne peut déposer qu'un exercice par session | Un second `POST /api/exercices` pour la même (session, étudiant) renvoie `409 EXERCICE_DEJA_DEPOSE` | Must |
| EF7 | Le système assigne un relecteur au hasard parmi les étudiants présents à la session, à l'exclusion de l'auteur | À la création d'un exercice, une relecture est créée avec un relecteurId ≠ etudiantId et parmi les présences de la session | Must |
| EF8 | Le relecteur note et commente un exercice | `POST /api/relectures/{id}` avec `note` ∈ [0,20] entier renvoie `200` et passe l'exercice au statut `RELU` | Must |
| EF9 | Le formateur consulte son tableau de bord par promotion | `GET /api/tableau?promotionId=X` renvoie un tableau avec presences, exercicesDeposes, moyenne et relecturesEnAttente par étudiant | Must |
| EF10 | Le formateur ajoute une présence manuellement | `POST /api/presences` avec `source=FORMATEUR` renvoie `201` et la présence est marquée comme telle | Should |
| EF11 | Le formateur clôture une session | `POST /api/sessions/{id}/cloture` renvoie `200` et empêche toute nouvelle écriture sur cette session | Should |
| EF12 | L'étudiant consulte sa note et son commentaire, sans le nom du relecteur | `GET /api/exercices/{id}/relecture` renvoie note + commentaire, **sans** exposer relecteurId | Could |

---

## 5. Exigences non fonctionnelles

| Réf | Exigence | Comment on la vérifie |
|---|---|---|
| ENF1 | Temps de réponse < 500 ms en P95 sur les endpoints GET du tableau, avec 100 étudiants et 10 sessions | Test de charge ou mesure manuelle en démo |
| ENF2 | Interface utilisable sur mobile (largeur ≥ 360 px) | Test manuel sur écran réduit |
| ENF3 | Démarrage chez un tiers en ≤ 3 commandes documentées (ou `docker compose up`) | Test depuis un clone vierge |
| ENF4 | Données de démonstration chargées au démarrage (1 promotion, ~10 étudiants, 1 formateur) | Vérification visuelle après démarrage |
| ENF5 | Format d'erreur homogène : `{ "code": "...", "message": "..." }` sur toutes les erreurs | Contrôle croisé avec le contrat |
| ENF6 | Aucune stack trace ne remonte au client | Revue des réponses d'erreur |

---

## 6. Règles de gestion

| Réf | Règle | Source |
|---|---|---|
| RG1 | Le code de présence expire 15 minutes après l'ouverture de la session | Q2 |
| RG2 | Aucune présence ne peut être marquée après la fin de la session | Q3 |
| RG3 | Après 5 erreurs de code, l'étudiant est bloqué 2 minutes (par session) | Q4 |
| RG4 | Un étudiant ne peut jamais être relecteur de son propre exercice | Q5 |
| RG5 | Un exercice n'a qu'un seul relecteur | Q6 |
| RG6 | Le relecteur est choisi au hasard parmi les étudiants présents à la session | Q7 |
| RG7 | L'étudiant relu voit sa note et le commentaire, mais jamais l'identité du relecteur | Q8 |
| RG8 | La note est un entier entre 0 et 20 | Q9 |
| RG9 | Le relecteur peut modifier sa note tant que la session n'est pas clôturée | Q10 |
| RG10 | Si le relecteur ne rend pas sa relecture, l'exercice reste en attente et apparaît clairement dans le tableau du formateur | Q11 |
| RG11 | Le dépôt d'un exercice reste possible jusqu'à la clôture de la session | Q12 |
| RG12 | Le lien d'un exercice peut être remplacé tant qu'aucune relecture n'a commencé (statut ≠ EN_ATTENTE_RELECTURE ni RELU) | Q13 |
| RG13 | Une présence ajoutée par le formateur porte `source=FORMATEUR` | Q14 |
| RG14 | Le tableau de bord affiche par étudiant : présences, exercices déposés, moyenne des notes reçues, relectures en attente | Q16 |

---

## 7. Zones d'ombre, hypothèses et contradictions tranchées

| Point | Réponse client (Qx) ou hypothèse | Décision retenue | Pourquoi |
|---|---|---|---|
| **Q10 vs Q15** (contradiction explicite) | Q10 : le relecteur peut modifier tant que la session n'est pas clôturée. Q15 : la note est définitive dès qu'elle est envoyée. | **Je retiens Q10** : la note reste modifiable jusqu'à la clôture de la session. | Q15 est une intention morale, Q10 décrit un usage réel (corriger une erreur). C'est cohérent avec Q11/Q12 qui maintiennent de la flexibilité tant que la session est ouverte. |
| **Q7 × Q12** (trou non vu) | Q7 : relecteur choisi parmi les présents. Q12 : dépôt possible après la fin de la session. | **Si aucun relecteur éligible n'existe** (seul étudiant, ou dépôt après la fin), l'exercice reste au statut `DEPOSE` **sans relecteur assigné**, visible dans le tableau du formateur. L'assignation automatique est retentée à chaque nouvelle présence ou nouvelle clôture de session. | Un étudiant ne peut jamais être son propre relecteur (RG4). Il faut donc un état transitoire explicite plutôt que de bloquer le dépôt. |
| **Clôture de session** (opération absente du contrat imposé) | Le contrat impose 5 opérations mais RG9, RG11, RG12 dépendent d'une clôture qui n'y figure pas. | **J'ajoute** `POST /api/sessions/{id}/cloture` au contrat (champ `clotureeAt` sur `Session`). | Le sujet autorise explicitement les opérations supplémentaires : « les 5 opérations imposées plus celles dont tu as besoin ». |
| **Blocage Q4 : portée** | Q4 dit « bloquez-le deux minutes » sans préciser sur quoi. | Blocage **par (etudiantId, sessionId)** sur 5 tentatives échouées. | Limite le blocage à la session concernée, évite un blocage global abusif. |
| **RG12 : « relecture commencée »** | Q13 conditionne le remplacement du lien à « personne n'a commencé à le relire ». | Considérée comme commencée dès qu'une ligne `Relecture` existe pour l'exercice. | Traçable et sans ambiguïté. |
| **Q1 : identification sans mot de passe** | L'étudiant choisit son nom dans une liste. | Le frontend pioche l'`etudiantId` dans `GET /api/etudiants?promotionId=X`. Le backend fait confiance à l'`etudiantId` reçu. | Conforme à Q1 ; hors périmètre sécurité (exclu section 3). |
| **Q4 : contre-mesure au-delà de 2 min** | Q4 ne dit rien après le blocage. | Le compteur se réinitialise après le blocage, un nouveau cycle de 5 erreurs redéclenche 2 min. | Évite un blocage permanent involontaire. |

---

## 8. Contraintes techniques

**Backend (imposé) :**
- B1 — Java 17+, Maven, wrapper `mvnw` commité.
- B2 — Contrat `api/contrat.yaml` respecté à la lettre (chemins, verbes, statuts, format d'erreur).
- B3 — Séparation contrôleur / service / repository. Pas de requête DB dans un contrôleur. Pas d'entité JPA exposée en JSON (DTO obligatoires).
- B4 — Validation des entrées + `@RestControllerAdvice`. Aucune stack trace côté client.
- B5 — Schéma versionné par Flyway. `ddl-auto=update` interdit hors tests.
- B6 — Deux tests : un unitaire sur une règle métier (ex. RG4 auto-relecture interdite), un d'intégration sur un endpoint (ex. `POST /api/presences` cas nominal + cas 410).

**Frontend (choix) :**
- F1 — Angular, build qui passe.
- F2 — Trois écrans : formateur (ouvrir session + tableau), étudiant (présence + dépôt), relecteur (relecture).
- F3 — Couche API dédiée (services Angular), états de chargement et d'erreur gérés, aucune règle métier dupliquée (la moyenne vient de l'API).

**Démarrage :**
- `docker compose up` ou ≤ 3 commandes documentées dans le README.
- Données de démonstration au démarrage (Flyway V2).

---

## 9. Livrables

- `docs/CAHIER_DES_CHARGES.md` (ce document).
- `docs/diagrammes/D1-cas-utilisation.md`, `D2-classes.md`, `D3-sequence-presence.md`, `D4-etats-exercice.md` (bonus).
- `api/contrat.yaml` complété.
- Backlog en issues GitHub avec critères d'acceptation et priorités.
- `backend/` Spring Boot complet (contrôleurs, services, repositories, DTO, mappers, migrations Flyway, tests).
- `frontend/` Angular (3 écrans, services, guards).
- `docker-compose.yml` + Dockerfiles.
- `README.md` d'installation testé depuis un clone vierge.
- `CHANGELOG.md` cohérent avec l'historique Git.
- `JOURNAL.md` tenu au fil de l'eau.
- `SOUMISSION.md` téléversé sur la plateforme avant 18h00.

---

## 10. Démarche prévue

**Étape 1 (analyse)** — Production du CDC, des 4 diagrammes Mermaid, du backlog en issues, du contrat complété. Commit `[JALON] analyse` **avant tout code**.

**Étape 2 (v0.1)** — Implémentation uniquement des stories **Must**. Une branche par ticket, une PR par branche liée à l'issue, issues fermées par les commits. Commit `[JALON] v0.1`.

**Étape 3 (enveloppe)** — Ouverture du script `./enveloppe`. Création d'une issue « Bug signalé » **avant** de coder. Reproduction, migration Flyway supplémentaire, mise à jour du contrat, du CDC et des diagrammes dans des commits séparés (correctif ≠ évolution).

**Étape 4 (v1.0)** — Gel de la version, `CHANGELOG.md`, `README` d'installation testé depuis un clone vierge, tri du backlog restant. Commit `[JALON] v1.0`.

**Étape 5 (git-lab)** — Épreuve indépendante sur le bundle fourni. Résolution des 5 situations, push de toutes les branches sur `kfokam48-gitlab-192`.

**Étape 6 (soumission)** — Rédaction de `SOUMISSION.md` avec les hash complets des deux dépôts, téléversement sur la plateforme **avant 18h00**.

**Definition of Done d'un ticket :**
1. Le code compile et les tests passent localement.
2. La PR est ouverte et liée à une issue, avec un titre qui décrit le résultat.
3. Le critère d'acceptation de l'issue est **vérifié manuellement** (curl, Postman ou UI).
4. La branche est fusionnée dans `main`, `main` reste sain.
5. Si le ticket touche au contrat d'API, `api/contrat.yaml` est mis à jour dans le même commit.
