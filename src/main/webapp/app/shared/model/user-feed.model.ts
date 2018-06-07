import { Moment } from 'moment';

export interface IUserFeed {
  id?: number;
  body?: string;
  createAt?: Moment;
  updateAt?: Moment;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IUserFeed> = {};
