import { Moment } from 'moment';

export const enum PaymentStatusType {
  OPEN = 'OPEN',
  PENDING = 'PENDING',
  SUCCESS = 'SUCCESS',
  FAILED = 'FAILED'
}

export interface IPayment {
  id?: number;
  code?: string;
  money?: number;
  paidTime?: Moment;
  paymentStatus?: PaymentStatusType;
  createAt?: Moment;
  updateAt?: Moment;
  houseId?: number;
  customerLogin?: string;
  customerId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<IPayment> = {};
