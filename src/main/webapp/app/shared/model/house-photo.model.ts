import { Moment } from 'moment';

export interface IHousePhoto {
  id?: number;
  imageContentType?: string;
  image?: any;
  createAt?: Moment;
  houseId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<IHousePhoto> = {};
