import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lesson.reducer';

export const LessonDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const lessonEntity = useAppSelector(state => state.sam.lesson.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lessonDetailsHeading">
          <Translate contentKey="samApp.samLesson.detail.title">Lesson</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.id}</dd>
          <dt>
            <span id="teacherId">
              <Translate contentKey="samApp.samLesson.teacherId">Teacher Id</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.teacherId}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="samApp.samLesson.title">Title</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="samApp.samLesson.content">Content</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.content}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="samApp.samLesson.price">Price</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.price}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="samApp.samLesson.time">Time</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.time}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="samApp.samLesson.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.createdAt ? <TextFormat value={lessonEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="samApp.samLesson.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{lessonEntity.updatedAt ? <TextFormat value={lessonEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/sam/lesson" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sam/lesson/${lessonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LessonDetail;
