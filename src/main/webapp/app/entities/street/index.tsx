import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Street from './street';
import StreetDetail from './street-detail';
import StreetUpdate from './street-update';
import StreetDeleteDialog from './street-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={StreetUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={StreetUpdate} />
      <Route exact path={`${match.url}/:id`} component={StreetDetail} />
      <Route path={match.url} component={Street} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={StreetDeleteDialog} />
  </>
);

export default Routes;
