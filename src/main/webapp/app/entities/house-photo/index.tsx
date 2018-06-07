import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import HousePhoto from './house-photo';
import HousePhotoDetail from './house-photo-detail';
import HousePhotoUpdate from './house-photo-update';
import HousePhotoDeleteDialog from './house-photo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={HousePhotoUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={HousePhotoUpdate} />
      <Route exact path={`${match.url}/:id`} component={HousePhotoDetail} />
      <Route path={match.url} component={HousePhoto} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={HousePhotoDeleteDialog} />
  </>
);

export default Routes;
