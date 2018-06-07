import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import PotentialCustomer from './potential-customer';
import PotentialCustomerDetail from './potential-customer-detail';
import PotentialCustomerUpdate from './potential-customer-update';
import PotentialCustomerDeleteDialog from './potential-customer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={PotentialCustomerUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={PotentialCustomerUpdate} />
      <Route exact path={`${match.url}/:id`} component={PotentialCustomerDetail} />
      <Route path={match.url} component={PotentialCustomer} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={PotentialCustomerDeleteDialog} />
  </>
);

export default Routes;
