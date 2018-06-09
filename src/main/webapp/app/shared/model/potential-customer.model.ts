import { Moment } from 'moment';

export const enum CustomerLevel {
  NORMAL = 'NORMAL',
  SILVER = 'SILVER',
  GOLD = 'GOLD',
  PLATINUM = 'PLATINUM'
}

export interface IPotentialCustomer {
  id?: number;
  level?: CustomerLevel;
  description?: string;
  createAt?: Moment;
  updateAt?: Moment;
  customerLogin?: string;
  customerId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<IPotentialCustomer> = {};
