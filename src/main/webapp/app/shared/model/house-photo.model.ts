import { Moment } from 'moment';

export interface IHousePhoto {
  id?: number;
  name?: string;
  createAt?: Moment;
  houseId?: number;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IHousePhoto> = {};
