import { Moment } from 'moment';
import { IDistrict } from './district.model';

export interface IRegion {
  id?: number;
  name?: string;
  enabled?: boolean;
  createAt?: Moment;
  updateAt?: Moment;
  districts?: IDistrict[];
}

export const defaultValue: Readonly<IRegion> = {
  enabled: false
};
