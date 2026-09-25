export interface MarquerPresenceRequest {
  code: string;
  etudiantId: number;
}

export interface PresenceResponse {
  id: number;
  sessionId: number;
  etudiantId: number;
  source: 'ETUDIANT' | 'FORMATEUR';
}
