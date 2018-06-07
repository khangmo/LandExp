import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Banner from './banner';
import BannerDetail from './banner-detail';
import BannerUpdate from './banner-update';
import BannerDeleteDialog from './banner-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={BannerUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={BannerUpdate} />
      <Route exact path={`${match.url}/:id`} component={BannerDetail} />
      <Route path={match.url} component={Banner} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={BannerDeleteDialog} />
  </>
);

export default Routes;
