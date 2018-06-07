import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import House from './house';
import HouseDetail from './house-detail';
import HouseUpdate from './house-update';
import HouseDeleteDialog from './house-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={HouseUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={HouseUpdate} />
      <Route exact path={`${match.url}/:id`} component={HouseDetail} />
      <Route path={match.url} component={House} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={HouseDeleteDialog} />
  </>
);

export default Routes;
