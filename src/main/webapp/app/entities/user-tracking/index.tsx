import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import UserTracking from './user-tracking';
import UserTrackingDetail from './user-tracking-detail';
import UserTrackingUpdate from './user-tracking-update';
import UserTrackingDeleteDialog from './user-tracking-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={UserTrackingUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={UserTrackingUpdate} />
      <Route exact path={`${match.url}/:id`} component={UserTrackingDetail} />
      <Route path={match.url} component={UserTracking} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={UserTrackingDeleteDialog} />
  </>
);

export default Routes;
