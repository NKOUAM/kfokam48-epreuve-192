-- V1 — Schéma initial
-- Correspond au diagramme D2 (docs/diagrammes/D2-classes.md)

CREATE TABLE promotion (
                         id          BIGSERIAL PRIMARY KEY,
                         nom         VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE etudiant (
                        id            BIGSERIAL PRIMARY KEY,
                        nom           VARCHAR(150) NOT NULL,
                        promotion_id  BIGINT NOT NULL REFERENCES promotion(id)
);
CREATE INDEX idx_etudiant_promotion ON etudiant(promotion_id);

CREATE TABLE formateur (
                         id   BIGSERIAL PRIMARY KEY,
                         nom  VARCHAR(150) NOT NULL
);

CREATE TABLE session (
                       id             BIGSERIAL PRIMARY KEY,
                       titre          VARCHAR(200) NOT NULL,
                       code           VARCHAR(10)  NOT NULL,
                       ouverture_at   TIMESTAMP    NOT NULL,
                       expiration_at  TIMESTAMP    NOT NULL,
                       cloturee_at    TIMESTAMP,
                       promotion_id   BIGINT NOT NULL REFERENCES promotion(id),
                       formateur_id   BIGINT NOT NULL REFERENCES formateur(id)
);
CREATE INDEX idx_session_promotion ON session(promotion_id);
CREATE INDEX idx_session_code      ON session(code);

CREATE TABLE presence (
                        id           BIGSERIAL PRIMARY KEY,
                        session_id   BIGINT NOT NULL REFERENCES session(id),
                        etudiant_id  BIGINT NOT NULL REFERENCES etudiant(id),
                        source       VARCHAR(20) NOT NULL CHECK (source IN ('ETUDIANT', 'FORMATEUR')),
                        created_at   TIMESTAMP NOT NULL DEFAULT now(),
                        CONSTRAINT uk_presence_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE exercice (
                        id           BIGSERIAL PRIMARY KEY,
                        session_id   BIGINT NOT NULL REFERENCES session(id),
                        etudiant_id  BIGINT NOT NULL REFERENCES etudiant(id),
                        lien         VARCHAR(500) NOT NULL,
                        statut       VARCHAR(30) NOT NULL CHECK (statut IN ('DEPOSE', 'EN_ATTENTE_RELECTURE', 'RELU')),
                        CONSTRAINT uk_exercice_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE relecture (
                         id            BIGSERIAL PRIMARY KEY,
                         exercice_id   BIGINT NOT NULL REFERENCES exercice(id),
                         relecteur_id  BIGINT NOT NULL REFERENCES etudiant(id),
                         note          INTEGER CHECK (note IS NULL OR (note >= 0 AND note <= 20)),
                         commentaire   TEXT,
                         statut        VARCHAR(20) NOT NULL CHECK (statut IN ('EN_ATTENTE', 'RENDUE')),
                         created_at    TIMESTAMP NOT NULL DEFAULT now(),
                         updated_at    TIMESTAMP,
                         CONSTRAINT uk_relecture_exercice UNIQUE (exercice_id)
);
CREATE INDEX idx_relecture_relecteur ON relecture(relecteur_id);
