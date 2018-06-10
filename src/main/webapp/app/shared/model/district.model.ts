import { Moment } from 'moment';
import { IHouse } from './house.model';

export interface IDistrict {
  id?: number;
  name?: string;
  enabled?: boolean;
  createAt?: Moment;
  updateAt?: Moment;
  regionId?: number;
  houses?: IHouse[];
  cityName?: string;
  cityId?: number;
}

export const defaultValue: Readonly<IDistrict> = {
  enabled: false
};
