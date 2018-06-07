import { Moment } from 'moment';

export interface ICategory {
  id?: number;
  name?: string;
  nameAlias?: string;
  createAt?: Moment;
  updateAt?: Moment;
}

export const defaultValue: Readonly<ICategory> = {};
