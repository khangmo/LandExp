import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Category from './category';
import CategoryDetail from './category-detail';
import CategoryUpdate from './category-update';
import CategoryDeleteDialog from './category-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={CategoryUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={CategoryUpdate} />
      <Route exact path={`${match.url}/:id`} component={CategoryDetail} />
      <Route path={match.url} component={Category} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={CategoryDeleteDialog} />
  </>
);

export default Routes;
