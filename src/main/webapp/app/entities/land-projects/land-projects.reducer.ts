import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ILandProjects, defaultValue } from 'app/shared/model/land-projects.model';

export const ACTION_TYPES = {
  SEARCH_LANDPROJECTS: 'landProjects/SEARCH_LANDPROJECTS',
  FETCH_LANDPROJECTS_LIST: 'landProjects/FETCH_LANDPROJECTS_LIST',
  FETCH_LANDPROJECTS: 'landProjects/FETCH_LANDPROJECTS',
  CREATE_LANDPROJECTS: 'landProjects/CREATE_LANDPROJECTS',
  UPDATE_LANDPROJECTS: 'landProjects/UPDATE_LANDPROJECTS',
  DELETE_LANDPROJECTS: 'landProjects/DELETE_LANDPROJECTS',
  RESET: 'landProjects/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILandProjects>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type LandProjectsState = Readonly<typeof initialState>;

// Reducer

export default (state: LandProjectsState = initialState, action): LandProjectsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LANDPROJECTS):
    case REQUEST(ACTION_TYPES.FETCH_LANDPROJECTS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LANDPROJECTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LANDPROJECTS):
    case REQUEST(ACTION_TYPES.UPDATE_LANDPROJECTS):
    case REQUEST(ACTION_TYPES.DELETE_LANDPROJECTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_LANDPROJECTS):
    case FAILURE(ACTION_TYPES.FETCH_LANDPROJECTS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LANDPROJECTS):
    case FAILURE(ACTION_TYPES.CREATE_LANDPROJECTS):
    case FAILURE(ACTION_TYPES.UPDATE_LANDPROJECTS):
    case FAILURE(ACTION_TYPES.DELETE_LANDPROJECTS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LANDPROJECTS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANDPROJECTS_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANDPROJECTS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LANDPROJECTS):
    case SUCCESS(ACTION_TYPES.UPDATE_LANDPROJECTS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LANDPROJECTS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVER_API_URL + '/api/land-projects';
const apiSearchUrl = SERVER_API_URL + '/api/_search/land-projects';

// Actions

export const getSearchEntities: ICrudSearchAction<ILandProjects> = query => ({
  type: ACTION_TYPES.SEARCH_LANDPROJECTS,
  payload: axios.get<ILandProjects>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ILandProjects> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LANDPROJECTS_LIST,
    payload: axios.get<ILandProjects>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ILandProjects> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LANDPROJECTS,
    payload: axios.get<ILandProjects>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILandProjects> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LANDPROJECTS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILandProjects> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LANDPROJECTS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILandProjects> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LANDPROJECTS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
