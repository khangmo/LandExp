import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Region from './region';
import RegionDetail from './region-detail';
import RegionUpdate from './region-update';
import RegionDeleteDialog from './region-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={RegionUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={RegionUpdate} />
      <Route exact path={`${match.url}/:id`} component={RegionDetail} />
      <Route path={match.url} component={Region} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={RegionDeleteDialog} />
  </>
);

export default Routes;
