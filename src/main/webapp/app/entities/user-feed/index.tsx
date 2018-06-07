import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import UserFeed from './user-feed';
import UserFeedDetail from './user-feed-detail';
import UserFeedUpdate from './user-feed-update';
import UserFeedDeleteDialog from './user-feed-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={UserFeedUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={UserFeedUpdate} />
      <Route exact path={`${match.url}/:id`} component={UserFeedDetail} />
      <Route path={match.url} component={UserFeed} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={UserFeedDeleteDialog} />
  </>
);

export default Routes;
