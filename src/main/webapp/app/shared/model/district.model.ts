import { Moment } from 'moment';

export interface IDistrict {
  id?: number;
  name?: string;
  enabled?: boolean;
  createAt?: Moment;
  updateAt?: Moment;
  regionName?: string;
  regionId?: number;
  cityName?: string;
  cityId?: number;
}

export const defaultValue: Readonly<IDistrict> = {
  enabled: false
};
