export interface IUserProfile {
  id?: number;
  name?: string;
  phoneNumber?: string;
  userEmail?: string;
  userId?: number;
}

export const defaultValue: Readonly<IUserProfile> = {};
