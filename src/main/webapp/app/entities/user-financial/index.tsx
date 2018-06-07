import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import UserFinancial from './user-financial';
import UserFinancialDetail from './user-financial-detail';
import UserFinancialUpdate from './user-financial-update';
import UserFinancialDeleteDialog from './user-financial-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={UserFinancialUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={UserFinancialUpdate} />
      <Route exact path={`${match.url}/:id`} component={UserFinancialDetail} />
      <Route path={match.url} component={UserFinancial} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={UserFinancialDeleteDialog} />
  </>
);

export default Routes;
