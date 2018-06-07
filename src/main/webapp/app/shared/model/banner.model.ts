import { Moment } from 'moment';

export interface IBanner {
  id?: number;
  title?: string;
  titleAlias?: string;
  area?: number;
  hits?: number;
  publicUp?: Moment;
  publicDown?: Moment;
  createAt?: Moment;
  updateAt?: Moment;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<IBanner> = {};
