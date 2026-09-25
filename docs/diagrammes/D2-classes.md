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

    Promotion "1" --> "*" Etudiant : regroupe
    Promotion "1" --> "*" Session : programme
    Formateur "1" --> "*" Session : ouvre
    Session "1" --> "*" Presence : enregistre
    Etudiant "1" --> "*" Presence : marque
    Session "1" --> "*" Exercice : contient
    Etudiant "1" --> "*" Exercice : dépose
    Exercice "1" --> "0..1" Relecture : fait l'objet de
    Etudiant "1" --> "*" Relecture : rédige
