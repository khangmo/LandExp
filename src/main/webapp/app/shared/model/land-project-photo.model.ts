import { Moment } from 'moment';

export interface ILandProjectPhoto {
  id?: number;
  imageContentType?: string;
  image?: any;
  createAt?: Moment;
  landProjectId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<ILandProjectPhoto> = {};
