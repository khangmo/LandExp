import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import SearchTracking from './search-tracking';
import SearchTrackingDetail from './search-tracking-detail';
import SearchTrackingUpdate from './search-tracking-update';
import SearchTrackingDeleteDialog from './search-tracking-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={SearchTrackingUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={SearchTrackingUpdate} />
      <Route exact path={`${match.url}/:id`} component={SearchTrackingDetail} />
      <Route path={match.url} component={SearchTracking} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={SearchTrackingDeleteDialog} />
  </>
);

export default Routes;
