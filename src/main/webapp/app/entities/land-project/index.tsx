import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import LandProject from './land-project';
import LandProjectDetail from './land-project-detail';
import LandProjectUpdate from './land-project-update';
import LandProjectDeleteDialog from './land-project-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={LandProjectUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={LandProjectUpdate} />
      <Route exact path={`${match.url}/:id`} component={LandProjectDetail} />
      <Route path={match.url} component={LandProject} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={LandProjectDeleteDialog} />
  </>
);

export default Routes;
