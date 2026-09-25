# D1 — Diagramme de cas d'utilisation

Acteurs : **Formateur**, **Étudiant**, **Relecteur** (rôle porté par un étudiant), **Système** (acteur secondaire).

```mermaid
graph LR
    F[Formateur]
    E[Étudiant]
    R[Relecteur]
    S[Système]

    UC1((Ouvrir une session))
    UC2((Consulter le code))
    UC3((Ajouter une présence manuelle))
    UC4((Clôturer une session))
    UC5((Consulter le tableau))

    UC6((Marquer sa présence))
    UC7((Déposer un exercice))
    UC8((Remplacer le lien))
    UC9((Consulter sa note))

    UC10((Voir ses relectures))
    UC11((Noter et commenter))
    UC12((Corriger sa note))

    UC13((Assigner un relecteur))

    F --> UC1
    F --> UC2
    F --> UC3
    F --> UC4
    F --> UC5

    E --> UC6
    E --> UC7
    E --> UC8
    E --> UC9

    R --> UC10
    R --> UC11
    R --> UC12

    S --> UC13
