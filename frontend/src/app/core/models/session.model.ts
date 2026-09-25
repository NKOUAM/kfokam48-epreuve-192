export interface OuvrirSessionRequest {
  titre: string;
  promotionId: number;
}

export interface SessionResponse {
  id: number;
  code: string;
  ouvertureAt: string;
  expirationAt: string;
}
