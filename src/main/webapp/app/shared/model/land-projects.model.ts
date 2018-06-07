export interface ILandProjects {
  id?: number;
  name?: string;
  cityName?: string;
  cityId?: number;
  streetName?: string;
  streetId?: number;
  createByLogin?: string;
  createById?: number;
  updateByLogin?: string;
  updateById?: number;
}

export const defaultValue: Readonly<ILandProjects> = {};
