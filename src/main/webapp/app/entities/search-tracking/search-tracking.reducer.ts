import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ISearchTracking, defaultValue } from 'app/shared/model/search-tracking.model';

export const ACTION_TYPES = {
  SEARCH_SEARCHTRACKINGS: 'searchTracking/SEARCH_SEARCHTRACKINGS',
  FETCH_SEARCHTRACKING_LIST: 'searchTracking/FETCH_SEARCHTRACKING_LIST',
  FETCH_SEARCHTRACKING: 'searchTracking/FETCH_SEARCHTRACKING',
  CREATE_SEARCHTRACKING: 'searchTracking/CREATE_SEARCHTRACKING',
  UPDATE_SEARCHTRACKING: 'searchTracking/UPDATE_SEARCHTRACKING',
  DELETE_SEARCHTRACKING: 'searchTracking/DELETE_SEARCHTRACKING',
  RESET: 'searchTracking/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISearchTracking>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SearchTrackingState = Readonly<typeof initialState>;

// Reducer

export default (state: SearchTrackingState = initialState, action): SearchTrackingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SEARCHTRACKINGS):
    case REQUEST(ACTION_TYPES.FETCH_SEARCHTRACKING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SEARCHTRACKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SEARCHTRACKING):
    case REQUEST(ACTION_TYPES.UPDATE_SEARCHTRACKING):
    case REQUEST(ACTION_TYPES.DELETE_SEARCHTRACKING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SEARCHTRACKINGS):
    case FAILURE(ACTION_TYPES.FETCH_SEARCHTRACKING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SEARCHTRACKING):
    case FAILURE(ACTION_TYPES.CREATE_SEARCHTRACKING):
    case FAILURE(ACTION_TYPES.UPDATE_SEARCHTRACKING):
    case FAILURE(ACTION_TYPES.DELETE_SEARCHTRACKING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SEARCHTRACKINGS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SEARCHTRACKING_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SEARCHTRACKING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SEARCHTRACKING):
    case SUCCESS(ACTION_TYPES.UPDATE_SEARCHTRACKING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SEARCHTRACKING):
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

const apiUrl = SERVER_API_URL + '/api/search-trackings';
const apiSearchUrl = SERVER_API_URL + '/api/_search/search-trackings';

// Actions

export const getSearchEntities: ICrudSearchAction<ISearchTracking> = query => ({
  type: ACTION_TYPES.SEARCH_SEARCHTRACKINGS,
  payload: axios.get<ISearchTracking>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ISearchTracking> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SEARCHTRACKING_LIST,
    payload: axios.get<ISearchTracking>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISearchTracking> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SEARCHTRACKING,
    payload: axios.get<ISearchTracking>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISearchTracking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SEARCHTRACKING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISearchTracking> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SEARCHTRACKING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISearchTracking> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SEARCHTRACKING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
