import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import District from './district';
import DistrictDetail from './district-detail';
import DistrictUpdate from './district-update';
import DistrictDeleteDialog from './district-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={DistrictUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={DistrictUpdate} />
      <Route exact path={`${match.url}/:id`} component={DistrictDetail} />
      <Route path={match.url} component={District} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={DistrictDeleteDialog} />
  </>
);

export default Routes;
