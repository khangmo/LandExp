import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IUserTracking, defaultValue } from 'app/shared/model/user-tracking.model';

export const ACTION_TYPES = {
  SEARCH_USERTRACKINGS: 'userTracking/SEARCH_USERTRACKINGS',
  FETCH_USERTRACKING_LIST: 'userTracking/FETCH_USERTRACKING_LIST',
  FETCH_USERTRACKING: 'userTracking/FETCH_USERTRACKING',
  CREATE_USERTRACKING: 'userTracking/CREATE_USERTRACKING',
  UPDATE_USERTRACKING: 'userTracking/UPDATE_USERTRACKING',
  DELETE_USERTRACKING: 'userTracking/DELETE_USERTRACKING',
  RESET: 'userTracking/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserTracking>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UserTrackingState = Readonly<typeof initialState>;

// Reducer

export default (state: UserTrackingState = initialState, action): UserTrackingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_USERTRACKINGS):
    case REQUEST(ACTION_TYPES.FETCH_USERTRACKING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERTRACKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERTRACKING):
    case REQUEST(ACTION_TYPES.UPDATE_USERTRACKING):
    case REQUEST(ACTION_TYPES.DELETE_USERTRACKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_USERTRACKINGS):
    case FAILURE(ACTION_TYPES.FETCH_USERTRACKING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERTRACKING):
    case FAILURE(ACTION_TYPES.CREATE_USERTRACKING):
    case FAILURE(ACTION_TYPES.UPDATE_USERTRACKING):
    case FAILURE(ACTION_TYPES.DELETE_USERTRACKING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_USERTRACKINGS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERTRACKING_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERTRACKING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERTRACKING):
    case SUCCESS(ACTION_TYPES.UPDATE_USERTRACKING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERTRACKING):
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

const apiUrl = SERVER_API_URL + '/api/user-trackings';
const apiSearchUrl = SERVER_API_URL + '/api/_search/user-trackings';

// Actions

export const getSearchEntities: ICrudSearchAction<IUserTracking> = query => ({
  type: ACTION_TYPES.SEARCH_USERTRACKINGS,
  payload: axios.get<IUserTracking>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IUserTracking> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERTRACKING_LIST,
    payload: axios.get<IUserTracking>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUserTracking> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERTRACKING,
    payload: axios.get<IUserTracking>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserTracking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERTRACKING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserTracking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERTRACKING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserTracking> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERTRACKING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
