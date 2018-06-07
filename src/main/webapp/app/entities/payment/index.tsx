import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Payment from './payment';
import PaymentDetail from './payment-detail';
import PaymentUpdate from './payment-update';
import PaymentDeleteDialog from './payment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={PaymentUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={PaymentUpdate} />
      <Route exact path={`${match.url}/:id`} component={PaymentDetail} />
      <Route path={match.url} component={Payment} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={PaymentDeleteDialog} />
  </>
);

export default Routes;
