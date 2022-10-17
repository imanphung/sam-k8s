import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SocialNetWork from './social-net-work';
import SocialNetWorkDetail from './social-net-work-detail';
import SocialNetWorkUpdate from './social-net-work-update';
import SocialNetWorkDeleteDialog from './social-net-work-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SocialNetWorkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SocialNetWorkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SocialNetWorkDetail} />
      <ErrorBoundaryRoute path={match.url} component={SocialNetWork} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SocialNetWorkDeleteDialog} />
  </>
);

export default Routes;
