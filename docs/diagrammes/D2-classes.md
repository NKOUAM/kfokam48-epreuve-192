# D2 — Diagramme de classes (modèle de données)

Correspond aux migrations Flyway `V1__init_schema.sql` (voir `backend/src/main/resources/db/migration/`).

```mermaid
classDiagram
    class Promotion {
        +Long id
        +String nom
    }

    class Etudiant {
        +Long id
        +String nom
        +Long promotionId
    }

    class Formateur {
        +Long id
        +String nom
    }

    class Session {
        +Long id
        +String titre
        +String code
        +LocalDateTime ouvertureAt
        +LocalDateTime expirationAt
        +LocalDateTime clotureeAt
        +Long promotionId
        +Long formateurId
    }

    class Presence {
        +Long id
        +Long sessionId
        +Long etudiantId
        +SourcePresence source
        +LocalDateTime createdAt
    }

    class Exercice {
        +Long id
        +Long sessionId
        +Long etudiantId
        +String lien
        +StatutExercice statut
    }

    class Relecture {
        +Long id
        +Long exerciceId
        +Long relecteurId
        +Integer note
        +String commentaire
        +StatutRelecture statut
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }

    class SourcePresence {
        <<enumeration>>
        ETUDIANT
        FORMATEUR
    }

    class StatutExercice {
        <<enumeration>>
        DEPOSE
        EN_ATTENTE_RELECTURE
        RELU
    }

    class StatutRelecture {
        <<enumeration>>
        EN_ATTENTE
        RENDUE
    }

    Promotion "1" --> "*" Etudiant : regroupe
    Promotion "1" --> "*" Session : programme
    Formateur "1" --> "*" Session : ouvre
    Session "1" --> "*" Presence : enregistre
    Etudiant "1" --> "*" Presence : marque
    Session "1" --> "*" Exercice : contient
    Etudiant "1" --> "*" Exercice : dépose
    Exercice "1" --> "0..1" Relecture : fait l'objet de
    Etudiant "1" --> "*" Relecture : rédige
```

**Contraintes d'unicité (posées en V1) :**
- `UNIQUE(session_id, etudiant_id)` sur `presence` — RG « déjà présent » (409)
- `UNIQUE(session_id, etudiant_id)` sur `exercice` — RG « exercice déjà déposé » (409)
- `UNIQUE(exercice_id)` sur `relecture` — RG5 « un seul relecteur »

**Cardinalité notable :** `Exercice "1" --> "0..1" Relecture` — un exercice peut exister **sans** relecture (statut `DEPOSE`, cas Q7 × Q12, voir CDC section 7).
