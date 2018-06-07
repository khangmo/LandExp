import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import HouseTracking from './house-tracking';
import HouseTrackingDetail from './house-tracking-detail';
import HouseTrackingUpdate from './house-tracking-update';
import HouseTrackingDeleteDialog from './house-tracking-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={HouseTrackingUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={HouseTrackingUpdate} />
      <Route exact path={`${match.url}/:id`} component={HouseTrackingDetail} />
      <Route path={match.url} component={HouseTracking} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={HouseTrackingDeleteDialog} />
  </>
);

export default Routes;
