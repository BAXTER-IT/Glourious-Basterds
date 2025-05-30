export interface User {
  username: string;
  password?: string;
  token?: string;
}

export interface AuthResponse {
  success: boolean;
  user?: User;
  message?: string;
}