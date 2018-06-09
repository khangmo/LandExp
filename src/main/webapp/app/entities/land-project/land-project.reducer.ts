import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ILandProject, defaultValue } from 'app/shared/model/land-project.model';

export const ACTION_TYPES = {
  SEARCH_LANDPROJECTS: 'landProject/SEARCH_LANDPROJECTS',
  FETCH_LANDPROJECT_LIST: 'landProject/FETCH_LANDPROJECT_LIST',
  FETCH_LANDPROJECT: 'landProject/FETCH_LANDPROJECT',
  CREATE_LANDPROJECT: 'landProject/CREATE_LANDPROJECT',
  UPDATE_LANDPROJECT: 'landProject/UPDATE_LANDPROJECT',
  DELETE_LANDPROJECT: 'landProject/DELETE_LANDPROJECT',
  SET_BLOB: 'landProject/SET_BLOB',
  RESET: 'landProject/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILandProject>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type LandProjectState = Readonly<typeof initialState>;

// Reducer

export default (state: LandProjectState = initialState, action): LandProjectState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LANDPROJECTS):
    case REQUEST(ACTION_TYPES.FETCH_LANDPROJECT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LANDPROJECT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LANDPROJECT):
    case REQUEST(ACTION_TYPES.UPDATE_LANDPROJECT):
    case REQUEST(ACTION_TYPES.DELETE_LANDPROJECT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_LANDPROJECTS):
    case FAILURE(ACTION_TYPES.FETCH_LANDPROJECT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LANDPROJECT):
    case FAILURE(ACTION_TYPES.CREATE_LANDPROJECT):
    case FAILURE(ACTION_TYPES.UPDATE_LANDPROJECT):
    case FAILURE(ACTION_TYPES.DELETE_LANDPROJECT):
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
    case SUCCESS(ACTION_TYPES.FETCH_LANDPROJECT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANDPROJECT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LANDPROJECT):
    case SUCCESS(ACTION_TYPES.UPDATE_LANDPROJECT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LANDPROJECT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
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

export const getSearchEntities: ICrudSearchAction<ILandProject> = query => ({
  type: ACTION_TYPES.SEARCH_LANDPROJECTS,
  payload: axios.get<ILandProject>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ILandProject> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LANDPROJECT_LIST,
    payload: axios.get<ILandProject>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ILandProject> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LANDPROJECT,
    payload: axios.get<ILandProject>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILandProject> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LANDPROJECT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILandProject> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LANDPROJECT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILandProject> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LANDPROJECT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
