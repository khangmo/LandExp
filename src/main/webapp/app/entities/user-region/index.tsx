import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import UserRegion from './user-region';
import UserRegionDetail from './user-region-detail';
import UserRegionUpdate from './user-region-update';
import UserRegionDeleteDialog from './user-region-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={UserRegionUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={UserRegionUpdate} />
      <Route exact path={`${match.url}/:id`} component={UserRegionDetail} />
      <Route path={match.url} component={UserRegion} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={UserRegionDeleteDialog} />
  </>
);

export default Routes;
