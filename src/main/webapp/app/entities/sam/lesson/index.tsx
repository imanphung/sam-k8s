import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lesson from './lesson';
import LessonDetail from './lesson-detail';
import LessonUpdate from './lesson-update';
import LessonDeleteDialog from './lesson-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LessonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LessonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LessonDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lesson} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LessonDeleteDialog} />
  </>
);

export default Routes;
