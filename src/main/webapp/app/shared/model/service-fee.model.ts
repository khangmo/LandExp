export const enum SaleType {
  SALE_BY_MYSELF = 'SALE_BY_MYSELF',
  SALE_SUPPORT = 'SALE_SUPPORT'
}

export interface IServiceFee {
  id?: number;
  saleType?: SaleType;
  fee?: number;
}

export const defaultValue: Readonly<IServiceFee> = {};
