import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import LandProjectPhoto from './land-project-photo';
import LandProjectPhotoDetail from './land-project-photo-detail';
import LandProjectPhotoUpdate from './land-project-photo-update';
import LandProjectPhotoDeleteDialog from './land-project-photo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={LandProjectPhotoUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={LandProjectPhotoUpdate} />
      <Route exact path={`${match.url}/:id`} component={LandProjectPhotoDetail} />
      <Route path={match.url} component={LandProjectPhoto} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={LandProjectPhotoDeleteDialog} />
  </>
);

export default Routes;
