import * as React from 'react';
// tslint:disable-next-line:no-unused-variable
import { Route, Switch } from 'react-router-dom';

import Region from './region';
import City from './city';
import District from './district';
import Street from './street';
import UserRegion from './user-region';
import House from './house';
import ServiceFee from './service-fee';
import HouseTracking from './house-tracking';
import HousePhoto from './house-photo';
import LandProjects from './land-projects';
import Article from './article';
import Category from './category';
import UserProfile from './user-profile';
import UserSubscription from './user-subscription';
import UserTracking from './user-tracking';
import UserFeed from './user-feed';
import SearchTracking from './search-tracking';
import UserFinancial from './user-financial';
import PotentialCustomer from './potential-customer';
import Banner from './banner';
import Payment from './payment';
import LandProject from './land-project';
import LandProjectPhoto from './land-project-photo';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <Route path={`${match.url}/region`} component={Region} />
      <Route path={`${match.url}/city`} component={City} />
      <Route path={`${match.url}/district`} component={District} />
      <Route path={`${match.url}/street`} component={Street} />
      <Route path={`${match.url}/user-region`} component={UserRegion} />
      <Route path={`${match.url}/house`} component={House} />
      <Route path={`${match.url}/service-fee`} component={ServiceFee} />
      <Route path={`${match.url}/house-tracking`} component={HouseTracking} />
      <Route path={`${match.url}/house-photo`} component={HousePhoto} />
      <Route path={`${match.url}/land-projects`} component={LandProjects} />
      <Route path={`${match.url}/article`} component={Article} />
      <Route path={`${match.url}/category`} component={Category} />
      <Route path={`${match.url}/user-profile`} component={UserProfile} />
      <Route path={`${match.url}/user-subscription`} component={UserSubscription} />
      <Route path={`${match.url}/user-tracking`} component={UserTracking} />
      <Route path={`${match.url}/user-feed`} component={UserFeed} />
      <Route path={`${match.url}/search-tracking`} component={SearchTracking} />
      <Route path={`${match.url}/user-financial`} component={UserFinancial} />
      <Route path={`${match.url}/potential-customer`} component={PotentialCustomer} />
      <Route path={`${match.url}/banner`} component={Banner} />
      <Route path={`${match.url}/payment`} component={Payment} />
      <Route path={`${match.url}/land-project`} component={LandProject} />
      <Route path={`${match.url}/land-project-photo`} component={LandProjectPhoto} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
