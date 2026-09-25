# D3 — Séquence : marquer sa présence

Cas nominal + deux cas d'erreur (RG1 : code expiré, RG « déjà présent »). Correspond aux codes HTTP du contrat.

```mermaid
sequenceDiagram
    participant E as Étudiant
    participant F as Front (Angular)
    participant API as PresenceController
    participant S as PresenceService
    participant R as PresenceRepository

    E->>F: saisit le code
    F->>API: POST /api/presences { code, etudiantId }
    API->>S: enregistrer(code, etudiantId)
    S->>R: findByCode(code)

    alt code inconnu (400)
        R-->>S: absent
        S-->>API: CodeInconnuException
        API-->>F: 400 { code: "CODE_INCONNU" }
    else code expiré (410, RG1)
        R-->>S: Session
        S->>S: expirationAt < now()
        S-->>API: CodeExpireException
        API-->>F: 410 { code: "CODE_EXPIRE" }
    else déjà présent (409)
        R-->>S: Session
        S->>R: existsBySessionIdAndEtudiantId
        R-->>S: true
        S-->>API: DejaPresentException
        API-->>F: 409 { code: "DEJA_PRESENT" }
    else cas nominal (201)
        R-->>S: Session valide
        S->>R: save(Presence)
        R-->>S: Presence
        S-->>API: Presence
        API-->>F: 201 { id, sessionId, etudiantId, source: "ETUDIANT" }
    end
```

**Cas d'erreur couverts :** code inconnu, code expiré (RG1), déjà présent.
