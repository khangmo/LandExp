import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IPotentialCustomer, defaultValue } from 'app/shared/model/potential-customer.model';

export const ACTION_TYPES = {
  SEARCH_POTENTIALCUSTOMERS: 'potentialCustomer/SEARCH_POTENTIALCUSTOMERS',
  FETCH_POTENTIALCUSTOMER_LIST: 'potentialCustomer/FETCH_POTENTIALCUSTOMER_LIST',
  FETCH_POTENTIALCUSTOMER: 'potentialCustomer/FETCH_POTENTIALCUSTOMER',
  CREATE_POTENTIALCUSTOMER: 'potentialCustomer/CREATE_POTENTIALCUSTOMER',
  UPDATE_POTENTIALCUSTOMER: 'potentialCustomer/UPDATE_POTENTIALCUSTOMER',
  DELETE_POTENTIALCUSTOMER: 'potentialCustomer/DELETE_POTENTIALCUSTOMER',
  RESET: 'potentialCustomer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPotentialCustomer>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PotentialCustomerState = Readonly<typeof initialState>;

// Reducer

export default (state: PotentialCustomerState = initialState, action): PotentialCustomerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_POTENTIALCUSTOMERS):
    case REQUEST(ACTION_TYPES.FETCH_POTENTIALCUSTOMER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POTENTIALCUSTOMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POTENTIALCUSTOMER):
    case REQUEST(ACTION_TYPES.UPDATE_POTENTIALCUSTOMER):
    case REQUEST(ACTION_TYPES.DELETE_POTENTIALCUSTOMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_POTENTIALCUSTOMERS):
    case FAILURE(ACTION_TYPES.FETCH_POTENTIALCUSTOMER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POTENTIALCUSTOMER):
    case FAILURE(ACTION_TYPES.CREATE_POTENTIALCUSTOMER):
    case FAILURE(ACTION_TYPES.UPDATE_POTENTIALCUSTOMER):
    case FAILURE(ACTION_TYPES.DELETE_POTENTIALCUSTOMER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_POTENTIALCUSTOMERS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_POTENTIALCUSTOMER_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_POTENTIALCUSTOMER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POTENTIALCUSTOMER):
    case SUCCESS(ACTION_TYPES.UPDATE_POTENTIALCUSTOMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POTENTIALCUSTOMER):
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

const apiUrl = SERVER_API_URL + '/api/potential-customers';
const apiSearchUrl = SERVER_API_URL + '/api/_search/potential-customers';

// Actions

export const getSearchEntities: ICrudSearchAction<IPotentialCustomer> = query => ({
  type: ACTION_TYPES.SEARCH_POTENTIALCUSTOMERS,
  payload: axios.get<IPotentialCustomer>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IPotentialCustomer> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_POTENTIALCUSTOMER_LIST,
    payload: axios.get<IPotentialCustomer>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPotentialCustomer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POTENTIALCUSTOMER,
    payload: axios.get<IPotentialCustomer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPotentialCustomer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POTENTIALCUSTOMER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPotentialCustomer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POTENTIALCUSTOMER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPotentialCustomer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POTENTIALCUSTOMER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
