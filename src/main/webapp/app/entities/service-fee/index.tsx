import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import ServiceFee from './service-fee';
import ServiceFeeDetail from './service-fee-detail';
import ServiceFeeUpdate from './service-fee-update';
import ServiceFeeDeleteDialog from './service-fee-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={ServiceFeeUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={ServiceFeeUpdate} />
      <Route exact path={`${match.url}/:id`} component={ServiceFeeDetail} />
      <Route path={match.url} component={ServiceFee} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={ServiceFeeDeleteDialog} />
  </>
);

export default Routes;
