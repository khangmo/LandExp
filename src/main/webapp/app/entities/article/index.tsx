import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Article from './article';
import ArticleDetail from './article-detail';
import ArticleUpdate from './article-update';
import ArticleDeleteDialog from './article-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={ArticleUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={ArticleUpdate} />
      <Route exact path={`${match.url}/:id`} component={ArticleDetail} />
      <Route path={match.url} component={Article} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={ArticleDeleteDialog} />
  </>
);

export default Routes;
