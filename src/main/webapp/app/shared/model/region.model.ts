import { Moment } from 'moment';
import { IDistrict } from './district.model';
import { IUser } from './user.model';

export interface IRegion {
  id?: number;
  name?: string;
  enabled?: boolean;
  createAt?: Moment;
  updateAt?: Moment;
  districts?: IDistrict[];
  users?: IUser[];
}

export const defaultValue: Readonly<IRegion> = {
  enabled: false
};
