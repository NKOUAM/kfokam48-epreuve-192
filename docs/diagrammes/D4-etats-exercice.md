# D4 — États-transitions du cycle de vie d'un exercice (bonus)

```mermaid
stateDiagram-v2
    [*] --> DEPOSE : POST /api/exercices (EF5)
    DEPOSE --> EN_ATTENTE_RELECTURE : assignation relecteur (EF7)
    DEPOSE --> DEPOSE : aucun relecteur éligible (Q7 × Q12)
    EN_ATTENTE_RELECTURE --> RELU : POST /api/relectures/{id} (EF8)
    RELU --> EN_ATTENTE_RELECTURE : correction tant que session ouverte (RG9, Q10)
    RELU --> [*]

    note right of DEPOSE
        Aucun relecteur assigné.
        Visible dans le tableau (Q11).
    end note
```
