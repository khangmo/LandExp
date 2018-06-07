import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IUserRegion, defaultValue } from 'app/shared/model/user-region.model';

export const ACTION_TYPES = {
  SEARCH_USERREGIONS: 'userRegion/SEARCH_USERREGIONS',
  FETCH_USERREGION_LIST: 'userRegion/FETCH_USERREGION_LIST',
  FETCH_USERREGION: 'userRegion/FETCH_USERREGION',
  CREATE_USERREGION: 'userRegion/CREATE_USERREGION',
  UPDATE_USERREGION: 'userRegion/UPDATE_USERREGION',
  DELETE_USERREGION: 'userRegion/DELETE_USERREGION',
  RESET: 'userRegion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserRegion>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UserRegionState = Readonly<typeof initialState>;

// Reducer

export default (state: UserRegionState = initialState, action): UserRegionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_USERREGIONS):
    case REQUEST(ACTION_TYPES.FETCH_USERREGION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERREGION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERREGION):
    case REQUEST(ACTION_TYPES.UPDATE_USERREGION):
    case REQUEST(ACTION_TYPES.DELETE_USERREGION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_USERREGIONS):
    case FAILURE(ACTION_TYPES.FETCH_USERREGION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERREGION):
    case FAILURE(ACTION_TYPES.CREATE_USERREGION):
    case FAILURE(ACTION_TYPES.UPDATE_USERREGION):
    case FAILURE(ACTION_TYPES.DELETE_USERREGION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_USERREGIONS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERREGION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERREGION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERREGION):
    case SUCCESS(ACTION_TYPES.UPDATE_USERREGION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERREGION):
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

const apiUrl = SERVER_API_URL + '/api/user-regions';
const apiSearchUrl = SERVER_API_URL + '/api/_search/user-regions';

// Actions

export const getSearchEntities: ICrudSearchAction<IUserRegion> = query => ({
  type: ACTION_TYPES.SEARCH_USERREGIONS,
  payload: axios.get<IUserRegion>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IUserRegion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERREGION_LIST,
    payload: axios.get<IUserRegion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUserRegion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERREGION,
    payload: axios.get<IUserRegion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserRegion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERREGION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserRegion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERREGION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserRegion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERREGION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
