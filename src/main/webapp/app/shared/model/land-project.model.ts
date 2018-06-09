import { ILandProjectPhoto } from './land-project-photo.model';

export interface ILandProject {
  id?: number;
  name?: string;
  imageContentType?: string;
  image?: any;
  photos?: ILandProjectPhoto[];
  cityName?: string;
  cityId?: number;
  streetName?: string;
  streetId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<ILandProject> = {};
