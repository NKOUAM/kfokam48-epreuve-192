-- V2 — Données de démonstration (ENF4)

INSERT INTO promotion (id, nom) VALUES
                                  (1, 'KFOKAM48 — Yaoundé 2026'),
                                  (2, 'KFOKAM48 — Douala 2026');

INSERT INTO etudiant (id, nom, promotion_id) VALUES
                                               (1,  'Alice MBALLA',        1),
                                               (2,  'Bernard NGONO',       1),
                                               (3,  'Carla TCHOUTA',       1),
                                               (4,  'David FOTSO',         1),
                                               (5,  'Estelle NKOUM',       1),
                                               (6,  'Franck BIYA',         1),
                                               (7,  'Grace ATANGANA',      1),
                                               (8,  'Hervé KAMGA',         1),
                                               (9,  'Ines MOUSSA',         1),
                                               (10, 'Jacques ESSOMBA',     1),
                                               (11, 'Katia NDJOCK',        2),
                                               (12, 'Luc OWONA',           2);

INSERT INTO formateur (id, nom) VALUES
  (1, 'M. KFOKAM');

-- Remise à niveau des séquences pour éviter les collisions
SELECT setval('promotion_id_seq',  (SELECT MAX(id) FROM promotion));
SELECT setval('etudiant_id_seq',   (SELECT MAX(id) FROM etudiant));
SELECT setval('formateur_id_seq',  (SELECT MAX(id) FROM formateur));
