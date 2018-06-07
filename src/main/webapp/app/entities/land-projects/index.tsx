import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import LandProjects from './land-projects';
import LandProjectsDetail from './land-projects-detail';
import LandProjectsUpdate from './land-projects-update';
import LandProjectsDeleteDialog from './land-projects-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={LandProjectsUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={LandProjectsUpdate} />
      <Route exact path={`${match.url}/:id`} component={LandProjectsDetail} />
      <Route path={match.url} component={LandProjects} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={LandProjectsDeleteDialog} />
  </>
);

export default Routes;
