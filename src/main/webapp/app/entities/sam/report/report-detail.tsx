import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './report.reducer';

export const ReportDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const reportEntity = useAppSelector(state => state.sam.report.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reportDetailsHeading">
          <Translate contentKey="samApp.samReport.detail.title">Report</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reportEntity.id}</dd>
          <dt>
            <span id="studentId">
              <Translate contentKey="samApp.samReport.studentId">Student Id</Translate>
            </span>
          </dt>
          <dd>{reportEntity.studentId}</dd>
          <dt>
            <span id="lessonId">
              <Translate contentKey="samApp.samReport.lessonId">Lesson Id</Translate>
            </span>
          </dt>
          <dd>{reportEntity.lessonId}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="samApp.samReport.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{reportEntity.comment}</dd>
          <dt>
            <span id="star">
              <Translate contentKey="samApp.samReport.star">Star</Translate>
            </span>
          </dt>
          <dd>{reportEntity.star}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="samApp.samReport.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{reportEntity.createdAt ? <TextFormat value={reportEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="samApp.samReport.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{reportEntity.updatedAt ? <TextFormat value={reportEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/sam/report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sam/report/${reportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReportDetail;
