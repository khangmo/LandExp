export interface IUserProfile {
  id?: number;
  name?: string;
  phoneNumber?: string;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IUserProfile> = {};
