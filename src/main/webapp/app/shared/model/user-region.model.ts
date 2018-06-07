import { Moment } from 'moment';
import { IRegion } from './region.model';

export interface IUserRegion {
  id?: number;
  createAt?: Moment;
  updateAt?: Moment;
  userLogin?: string;
  userId?: number;
  regions?: IRegion[];
}

export const defaultValue: Readonly<IUserRegion> = {};
