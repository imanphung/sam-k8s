import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Deposit from './deposit';
import DepositDetail from './deposit-detail';
import DepositUpdate from './deposit-update';
import DepositDeleteDialog from './deposit-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DepositUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DepositUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DepositDetail} />
      <ErrorBoundaryRoute path={match.url} component={Deposit} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DepositDeleteDialog} />
  </>
);

export default Routes;
