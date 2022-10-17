import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './room.reducer';

export const RoomDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const roomEntity = useAppSelector(state => state.sam.room.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roomDetailsHeading">
          <Translate contentKey="samApp.samRoom.detail.title">Room</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{roomEntity.id}</dd>
          <dt>
            <span id="meetingId">
              <Translate contentKey="samApp.samRoom.meetingId">Meeting Id</Translate>
            </span>
          </dt>
          <dd>{roomEntity.meetingId}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="samApp.samRoom.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{roomEntity.createdBy}</dd>
          <dt>
            <span id="lessonId">
              <Translate contentKey="samApp.samRoom.lessonId">Lesson Id</Translate>
            </span>
          </dt>
          <dd>{roomEntity.lessonId}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="samApp.samRoom.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{roomEntity.startTime ? <TextFormat value={roomEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="samApp.samRoom.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{roomEntity.endTime ? <TextFormat value={roomEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="samApp.samRoom.status">Status</Translate>
            </span>
          </dt>
          <dd>{roomEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="samApp.samRoom.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{roomEntity.createdAt ? <TextFormat value={roomEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="samApp.samRoom.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{roomEntity.updatedAt ? <TextFormat value={roomEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/sam/room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sam/room/${roomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoomDetail;
