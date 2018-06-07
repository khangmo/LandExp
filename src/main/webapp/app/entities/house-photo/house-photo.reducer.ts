import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IHousePhoto, defaultValue } from 'app/shared/model/house-photo.model';

export const ACTION_TYPES = {
  SEARCH_HOUSEPHOTOS: 'housePhoto/SEARCH_HOUSEPHOTOS',
  FETCH_HOUSEPHOTO_LIST: 'housePhoto/FETCH_HOUSEPHOTO_LIST',
  FETCH_HOUSEPHOTO: 'housePhoto/FETCH_HOUSEPHOTO',
  CREATE_HOUSEPHOTO: 'housePhoto/CREATE_HOUSEPHOTO',
  UPDATE_HOUSEPHOTO: 'housePhoto/UPDATE_HOUSEPHOTO',
  DELETE_HOUSEPHOTO: 'housePhoto/DELETE_HOUSEPHOTO',
  RESET: 'housePhoto/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHousePhoto>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type HousePhotoState = Readonly<typeof initialState>;

// Reducer

export default (state: HousePhotoState = initialState, action): HousePhotoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_HOUSEPHOTOS):
    case REQUEST(ACTION_TYPES.FETCH_HOUSEPHOTO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HOUSEPHOTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HOUSEPHOTO):
    case REQUEST(ACTION_TYPES.UPDATE_HOUSEPHOTO):
    case REQUEST(ACTION_TYPES.DELETE_HOUSEPHOTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_HOUSEPHOTOS):
    case FAILURE(ACTION_TYPES.FETCH_HOUSEPHOTO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HOUSEPHOTO):
    case FAILURE(ACTION_TYPES.CREATE_HOUSEPHOTO):
    case FAILURE(ACTION_TYPES.UPDATE_HOUSEPHOTO):
    case FAILURE(ACTION_TYPES.DELETE_HOUSEPHOTO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_HOUSEPHOTOS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOUSEPHOTO_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOUSEPHOTO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HOUSEPHOTO):
    case SUCCESS(ACTION_TYPES.UPDATE_HOUSEPHOTO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HOUSEPHOTO):
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

const apiUrl = SERVER_API_URL + '/api/house-photos';
const apiSearchUrl = SERVER_API_URL + '/api/_search/house-photos';

// Actions

export const getSearchEntities: ICrudSearchAction<IHousePhoto> = query => ({
  type: ACTION_TYPES.SEARCH_HOUSEPHOTOS,
  payload: axios.get<IHousePhoto>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IHousePhoto> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HOUSEPHOTO_LIST,
    payload: axios.get<IHousePhoto>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IHousePhoto> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HOUSEPHOTO,
    payload: axios.get<IHousePhoto>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHousePhoto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HOUSEPHOTO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHousePhoto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HOUSEPHOTO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHousePhoto> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HOUSEPHOTO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
