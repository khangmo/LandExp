import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IHouseTracking, defaultValue } from 'app/shared/model/house-tracking.model';

export const ACTION_TYPES = {
  SEARCH_HOUSETRACKINGS: 'houseTracking/SEARCH_HOUSETRACKINGS',
  FETCH_HOUSETRACKING_LIST: 'houseTracking/FETCH_HOUSETRACKING_LIST',
  FETCH_HOUSETRACKING: 'houseTracking/FETCH_HOUSETRACKING',
  CREATE_HOUSETRACKING: 'houseTracking/CREATE_HOUSETRACKING',
  UPDATE_HOUSETRACKING: 'houseTracking/UPDATE_HOUSETRACKING',
  DELETE_HOUSETRACKING: 'houseTracking/DELETE_HOUSETRACKING',
  RESET: 'houseTracking/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHouseTracking>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type HouseTrackingState = Readonly<typeof initialState>;

// Reducer

export default (state: HouseTrackingState = initialState, action): HouseTrackingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_HOUSETRACKINGS):
    case REQUEST(ACTION_TYPES.FETCH_HOUSETRACKING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HOUSETRACKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HOUSETRACKING):
    case REQUEST(ACTION_TYPES.UPDATE_HOUSETRACKING):
    case REQUEST(ACTION_TYPES.DELETE_HOUSETRACKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_HOUSETRACKINGS):
    case FAILURE(ACTION_TYPES.FETCH_HOUSETRACKING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HOUSETRACKING):
    case FAILURE(ACTION_TYPES.CREATE_HOUSETRACKING):
    case FAILURE(ACTION_TYPES.UPDATE_HOUSETRACKING):
    case FAILURE(ACTION_TYPES.DELETE_HOUSETRACKING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_HOUSETRACKINGS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOUSETRACKING_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOUSETRACKING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HOUSETRACKING):
    case SUCCESS(ACTION_TYPES.UPDATE_HOUSETRACKING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HOUSETRACKING):
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

const apiUrl = SERVER_API_URL + '/api/house-trackings';
const apiSearchUrl = SERVER_API_URL + '/api/_search/house-trackings';

// Actions

export const getSearchEntities: ICrudSearchAction<IHouseTracking> = query => ({
  type: ACTION_TYPES.SEARCH_HOUSETRACKINGS,
  payload: axios.get<IHouseTracking>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IHouseTracking> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HOUSETRACKING_LIST,
    payload: axios.get<IHouseTracking>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IHouseTracking> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HOUSETRACKING,
    payload: axios.get<IHouseTracking>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHouseTracking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HOUSETRACKING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHouseTracking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HOUSETRACKING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHouseTracking> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HOUSETRACKING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
