export interface DeposerExerciceRequest {
  sessionId: number;
  etudiantId: number;
  lien: string;
}

export interface ExerciceResponse {
  id: number;
  statut: 'DEPOSE' | 'EN_ATTENTE_RELECTURE' | 'RELU';
}

export interface RelecturePubliqueResponse {
  note: number | null;
  commentaire: string | null;
  statut: string;
}
