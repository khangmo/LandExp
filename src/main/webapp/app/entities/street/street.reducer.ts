import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IStreet, defaultValue } from 'app/shared/model/street.model';

export const ACTION_TYPES = {
  SEARCH_STREETS: 'street/SEARCH_STREETS',
  FETCH_STREET_LIST: 'street/FETCH_STREET_LIST',
  FETCH_STREET: 'street/FETCH_STREET',
  CREATE_STREET: 'street/CREATE_STREET',
  UPDATE_STREET: 'street/UPDATE_STREET',
  DELETE_STREET: 'street/DELETE_STREET',
  RESET: 'street/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStreet>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type StreetState = Readonly<typeof initialState>;

// Reducer

export default (state: StreetState = initialState, action): StreetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_STREETS):
    case REQUEST(ACTION_TYPES.FETCH_STREET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STREET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STREET):
    case REQUEST(ACTION_TYPES.UPDATE_STREET):
    case REQUEST(ACTION_TYPES.DELETE_STREET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_STREETS):
    case FAILURE(ACTION_TYPES.FETCH_STREET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STREET):
    case FAILURE(ACTION_TYPES.CREATE_STREET):
    case FAILURE(ACTION_TYPES.UPDATE_STREET):
    case FAILURE(ACTION_TYPES.DELETE_STREET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_STREETS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STREET_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STREET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STREET):
    case SUCCESS(ACTION_TYPES.UPDATE_STREET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STREET):
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

const apiUrl = SERVER_API_URL + '/api/streets';
const apiSearchUrl = SERVER_API_URL + '/api/_search/streets';

// Actions

export const getSearchEntities: ICrudSearchAction<IStreet> = query => ({
  type: ACTION_TYPES.SEARCH_STREETS,
  payload: axios.get<IStreet>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IStreet> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STREET_LIST,
    payload: axios.get<IStreet>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IStreet> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STREET,
    payload: axios.get<IStreet>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStreet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STREET,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStreet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STREET,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStreet> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STREET,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
