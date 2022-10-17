import React from 'react';
import { Switch } from 'react-router-dom';
import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import entitiesReducers from './reducers';

import Report from './sam/report';
import SocialNetWork from './sam/social-net-work';
import Invoice from './sam/invoice';
import Customer from './sam/customer';
import Lesson from './sam/lesson';
import Room from './sam/room';
import Deposit from './sam/deposit';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  const store = getStore();
  store.injectReducer('sam', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}/report`} component={Report} />
        <ErrorBoundaryRoute path={`${match.url}/social-net-work`} component={SocialNetWork} />
        <ErrorBoundaryRoute path={`${match.url}/invoice`} component={Invoice} />
        <ErrorBoundaryRoute path={`${match.url}/customer`} component={Customer} />
        <ErrorBoundaryRoute path={`${match.url}/lesson`} component={Lesson} />
        <ErrorBoundaryRoute path={`${match.url}/room`} component={Room} />
        <ErrorBoundaryRoute path={`${match.url}/deposit`} component={Deposit} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
