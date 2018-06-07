import { Moment } from 'moment';

export const enum UserActionType {
  FOR_BUY = 'FOR_BUY',
  FOR_SELL = 'FOR_SELL',
  FOR_RENT = 'FOR_RENT',
  FOR_HIRE = 'FOR_HIRE'
}

export const enum MoneyType {
  MILLION = 'MILLION',
  HUNDRED_MILLION = 'HUNDRED_MILLION',
  BILLION = 'BILLION'
}

export const enum DirectionType {
  NORTH = 'NORTH',
  SOUTH = 'SOUTH',
  EAST = 'EAST',
  WEST = 'WEST',
  EAST_NORTH = 'EAST_NORTH',
  WEST_NORTH = 'WEST_NORTH',
  EAST_SOUTH = 'EAST_SOUTH',
  WEST_SOUTH = 'WEST_SOUTH'
}

export const enum LandType {
  APARTMENT = 'APARTMENT',
  PEN_HOUSE = 'PEN_HOUSE',
  HOME = 'HOME',
  HOME_VILLA = 'HOME_VILLA',
  HOME_STREET_SIDE = 'HOME_STREET_SIDE',
  MOTEL_ROOM = 'MOTEL_ROOM',
  OFFICE = 'OFFICE',
  LAND_SCAPE = 'LAND_SCAPE',
  LAND_OF_PROJECT = 'LAND_OF_PROJECT',
  LAND_FARM = 'LAND_FARM',
  LAND_RESORT = 'LAND_RESORT',
  WAREHOUSES = 'WAREHOUSES',
  KIOSKS = 'KIOSKS',
  OTHER = 'OTHER'
}

export const enum SaleType {
  SALE_BY_MYSELF = 'SALE_BY_MYSELF',
  SALE_SUPPORT = 'SALE_SUPPORT'
}

export const enum StatusType {
  PENDING = 'PENDING',
  PAID = 'PAID',
  APPROVED = 'APPROVED',
  CANCELED = 'CANCELED',
  EXPIRED = 'EXPIRED',
  SOLD = 'SOLD'
}

export interface IHouse {
  id?: number;
  actionType?: UserActionType;
  address?: string;
  money?: number;
  moneyType?: MoneyType;
  acreage?: number;
  discount?: number;
  direction?: DirectionType;
  directionBalcony?: DirectionType;
  floor?: string;
  numberOfFloor?: number;
  bathRoom?: number;
  parking?: boolean;
  furniture?: boolean;
  bedRoom?: number;
  landType?: LandType;
  saleType?: SaleType;
  fee?: number;
  feeMax?: number;
  hits?: number;
  statusType?: StatusType;
  createAt?: Moment;
  updateAt?: Moment;
  cityName?: string;
  cityId?: number;
  streetName?: string;
  streetId?: number;
  projectName?: string;
  projectId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<IHouse> = {
  parking: false,
  furniture: false
};
