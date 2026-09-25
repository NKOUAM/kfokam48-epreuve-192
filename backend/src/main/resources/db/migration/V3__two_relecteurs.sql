ALTER TABLE relecture DROP CONSTRAINT IF EXISTS uk_relecture_exercice;
ALTER TABLE relecture ADD CONSTRAINT uk_relecture_exercice_relecteur UNIQUE (exercice_id, relecteur_id);
ALTER TABLE exercice ADD COLUMN note_provisoire BOOLEAN NOT NULL DEFAULT FALSE;
