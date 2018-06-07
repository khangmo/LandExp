import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import UserProfile from './user-profile';
import UserProfileDetail from './user-profile-detail';
import UserProfileUpdate from './user-profile-update';
import UserProfileDeleteDialog from './user-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={UserProfileUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={UserProfileUpdate} />
      <Route exact path={`${match.url}/:id`} component={UserProfileDetail} />
      <Route path={match.url} component={UserProfile} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={UserProfileDeleteDialog} />
  </>
);

export default Routes;
