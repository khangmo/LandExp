import { Moment } from 'moment';

export interface IStreet {
  id?: number;
  name?: string;
  postalCode?: string;
  stateProvince?: string;
  enabled?: boolean;
  createAt?: Moment;
  updateAt?: Moment;
  districtName?: string;
  districtId?: number;
}

export const defaultValue: Readonly<IStreet> = {
  enabled: false
};
