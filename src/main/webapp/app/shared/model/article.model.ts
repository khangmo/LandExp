import { Moment } from 'moment';

export const enum StatusType {
  PENDING = 'PENDING',
  PAID = 'PAID',
  APPROVED = 'APPROVED',
  CANCELED = 'CANCELED',
  EXPIRED = 'EXPIRED',
  SOLD = 'SOLD'
}

export interface IArticle {
  id?: number;
  title?: string;
  titleAlias?: string;
  summary?: string;
  content?: string;
  statusType?: StatusType;
  hits?: number;
  createAt?: Moment;
  updateAt?: Moment;
  categoryName?: string;
  categoryId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<IArticle> = {};
