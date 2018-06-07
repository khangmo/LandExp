import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import UserSubscription from './user-subscription';
import UserSubscriptionDetail from './user-subscription-detail';
import UserSubscriptionUpdate from './user-subscription-update';
import UserSubscriptionDeleteDialog from './user-subscription-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={UserSubscriptionUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={UserSubscriptionUpdate} />
      <Route exact path={`${match.url}/:id`} component={UserSubscriptionDetail} />
      <Route path={match.url} component={UserSubscription} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={UserSubscriptionDeleteDialog} />
  </>
);

export default Routes;
