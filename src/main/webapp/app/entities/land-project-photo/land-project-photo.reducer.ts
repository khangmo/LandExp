import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ILandProjectPhoto, defaultValue } from 'app/shared/model/land-project-photo.model';

export const ACTION_TYPES = {
  SEARCH_LANDPROJECTPHOTOS: 'landProjectPhoto/SEARCH_LANDPROJECTPHOTOS',
  FETCH_LANDPROJECTPHOTO_LIST: 'landProjectPhoto/FETCH_LANDPROJECTPHOTO_LIST',
  FETCH_LANDPROJECTPHOTO: 'landProjectPhoto/FETCH_LANDPROJECTPHOTO',
  CREATE_LANDPROJECTPHOTO: 'landProjectPhoto/CREATE_LANDPROJECTPHOTO',
  UPDATE_LANDPROJECTPHOTO: 'landProjectPhoto/UPDATE_LANDPROJECTPHOTO',
  DELETE_LANDPROJECTPHOTO: 'landProjectPhoto/DELETE_LANDPROJECTPHOTO',
  SET_BLOB: 'landProjectPhoto/SET_BLOB',
  RESET: 'landProjectPhoto/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILandProjectPhoto>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LandProjectPhotoState = Readonly<typeof initialState>;

// Reducer

export default (state: LandProjectPhotoState = initialState, action): LandProjectPhotoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LANDPROJECTPHOTOS):
    case REQUEST(ACTION_TYPES.FETCH_LANDPROJECTPHOTO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LANDPROJECTPHOTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LANDPROJECTPHOTO):
    case REQUEST(ACTION_TYPES.UPDATE_LANDPROJECTPHOTO):
    case REQUEST(ACTION_TYPES.DELETE_LANDPROJECTPHOTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_LANDPROJECTPHOTOS):
    case FAILURE(ACTION_TYPES.FETCH_LANDPROJECTPHOTO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LANDPROJECTPHOTO):
    case FAILURE(ACTION_TYPES.CREATE_LANDPROJECTPHOTO):
    case FAILURE(ACTION_TYPES.UPDATE_LANDPROJECTPHOTO):
    case FAILURE(ACTION_TYPES.DELETE_LANDPROJECTPHOTO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LANDPROJECTPHOTOS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANDPROJECTPHOTO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANDPROJECTPHOTO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LANDPROJECTPHOTO):
    case SUCCESS(ACTION_TYPES.UPDATE_LANDPROJECTPHOTO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LANDPROJECTPHOTO):
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

const apiUrl = SERVER_API_URL + '/api/land-project-photos';
const apiSearchUrl = SERVER_API_URL + '/api/_search/land-project-photos';

// Actions

export const getSearchEntities: ICrudSearchAction<ILandProjectPhoto> = query => ({
  type: ACTION_TYPES.SEARCH_LANDPROJECTPHOTOS,
  payload: axios.get<ILandProjectPhoto>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ILandProjectPhoto> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LANDPROJECTPHOTO_LIST,
  payload: axios.get<ILandProjectPhoto>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILandProjectPhoto> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LANDPROJECTPHOTO,
    payload: axios.get<ILandProjectPhoto>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILandProjectPhoto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LANDPROJECTPHOTO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILandProjectPhoto> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LANDPROJECTPHOTO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILandProjectPhoto> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LANDPROJECTPHOTO,
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
