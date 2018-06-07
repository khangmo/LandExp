import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IUserFinancial, defaultValue } from 'app/shared/model/user-financial.model';

export const ACTION_TYPES = {
  SEARCH_USERFINANCIALS: 'userFinancial/SEARCH_USERFINANCIALS',
  FETCH_USERFINANCIAL_LIST: 'userFinancial/FETCH_USERFINANCIAL_LIST',
  FETCH_USERFINANCIAL: 'userFinancial/FETCH_USERFINANCIAL',
  CREATE_USERFINANCIAL: 'userFinancial/CREATE_USERFINANCIAL',
  UPDATE_USERFINANCIAL: 'userFinancial/UPDATE_USERFINANCIAL',
  DELETE_USERFINANCIAL: 'userFinancial/DELETE_USERFINANCIAL',
  RESET: 'userFinancial/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserFinancial>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UserFinancialState = Readonly<typeof initialState>;

// Reducer

export default (state: UserFinancialState = initialState, action): UserFinancialState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_USERFINANCIALS):
    case REQUEST(ACTION_TYPES.FETCH_USERFINANCIAL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERFINANCIAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERFINANCIAL):
    case REQUEST(ACTION_TYPES.UPDATE_USERFINANCIAL):
    case REQUEST(ACTION_TYPES.DELETE_USERFINANCIAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_USERFINANCIALS):
    case FAILURE(ACTION_TYPES.FETCH_USERFINANCIAL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERFINANCIAL):
    case FAILURE(ACTION_TYPES.CREATE_USERFINANCIAL):
    case FAILURE(ACTION_TYPES.UPDATE_USERFINANCIAL):
    case FAILURE(ACTION_TYPES.DELETE_USERFINANCIAL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_USERFINANCIALS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERFINANCIAL_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERFINANCIAL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERFINANCIAL):
    case SUCCESS(ACTION_TYPES.UPDATE_USERFINANCIAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERFINANCIAL):
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

const apiUrl = SERVER_API_URL + '/api/user-financials';
const apiSearchUrl = SERVER_API_URL + '/api/_search/user-financials';

// Actions

export const getSearchEntities: ICrudSearchAction<IUserFinancial> = query => ({
  type: ACTION_TYPES.SEARCH_USERFINANCIALS,
  payload: axios.get<IUserFinancial>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IUserFinancial> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERFINANCIAL_LIST,
    payload: axios.get<IUserFinancial>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUserFinancial> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERFINANCIAL,
    payload: axios.get<IUserFinancial>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserFinancial> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERFINANCIAL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserFinancial> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERFINANCIAL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserFinancial> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERFINANCIAL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
