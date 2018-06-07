import { Moment } from 'moment';

export interface IHousePhoto {
  id?: number;
  name?: string;
  createAt?: Moment;
}

export const defaultValue: Readonly<IHousePhoto> = {};
