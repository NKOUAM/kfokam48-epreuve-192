stateDiagram-v2
[*] --> DEPOSE : POST /api/exercices (EF5)
DEPOSE --> EN_ATTENTE_RELECTURE : assignation relecteur (EF7)
DEPOSE --> DEPOSE : aucun relecteur éligible (Q7 × Q12)
EN_ATTENTE_RELECTURE --> RELU : POST /api/relectures/{id} (EF8)
RELU --> EN_ATTENTE_RELECTURE : correction tant que session ouverte (RG9, Q10)
RELU --> [*]
