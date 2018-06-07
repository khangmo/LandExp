import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import City from './city';
import CityDetail from './city-detail';
import CityUpdate from './city-update';
import CityDeleteDialog from './city-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={CityUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={CityUpdate} />
      <Route exact path={`${match.url}/:id`} component={CityDetail} />
      <Route path={match.url} component={City} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={CityDeleteDialog} />
  </>
);

export default Routes;
