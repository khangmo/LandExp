import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { IUserFeed, defaultValue } from 'app/shared/model/user-feed.model';

export const ACTION_TYPES = {
  SEARCH_USERFEEDS: 'userFeed/SEARCH_USERFEEDS',
  FETCH_USERFEED_LIST: 'userFeed/FETCH_USERFEED_LIST',
  FETCH_USERFEED: 'userFeed/FETCH_USERFEED',
  CREATE_USERFEED: 'userFeed/CREATE_USERFEED',
  UPDATE_USERFEED: 'userFeed/UPDATE_USERFEED',
  DELETE_USERFEED: 'userFeed/DELETE_USERFEED',
  RESET: 'userFeed/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserFeed>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UserFeedState = Readonly<typeof initialState>;

// Reducer

export default (state: UserFeedState = initialState, action): UserFeedState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_USERFEEDS):
    case REQUEST(ACTION_TYPES.FETCH_USERFEED_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERFEED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERFEED):
    case REQUEST(ACTION_TYPES.UPDATE_USERFEED):
    case REQUEST(ACTION_TYPES.DELETE_USERFEED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_USERFEEDS):
    case FAILURE(ACTION_TYPES.FETCH_USERFEED_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERFEED):
    case FAILURE(ACTION_TYPES.CREATE_USERFEED):
    case FAILURE(ACTION_TYPES.UPDATE_USERFEED):
    case FAILURE(ACTION_TYPES.DELETE_USERFEED):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_USERFEEDS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERFEED_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERFEED):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERFEED):
    case SUCCESS(ACTION_TYPES.UPDATE_USERFEED):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERFEED):
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

const apiUrl = SERVER_API_URL + '/api/user-feeds';
const apiSearchUrl = SERVER_API_URL + '/api/_search/user-feeds';

// Actions

export const getSearchEntities: ICrudSearchAction<IUserFeed> = query => ({
  type: ACTION_TYPES.SEARCH_USERFEEDS,
  payload: axios.get<IUserFeed>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IUserFeed> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERFEED_LIST,
    payload: axios.get<IUserFeed>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUserFeed> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERFEED,
    payload: axios.get<IUserFeed>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserFeed> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERFEED,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserFeed> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERFEED,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserFeed> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERFEED,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
