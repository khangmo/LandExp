import { Moment } from 'moment';

export interface IRegion {
  id?: number;
  name?: string;
  enabled?: boolean;
  createAt?: Moment;
  updateAt?: Moment;
}

export const defaultValue: Readonly<IRegion> = {
  enabled: false
};
