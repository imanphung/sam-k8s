import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './social-net-work.reducer';

export const SocialNetWorkDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const socialNetWorkEntity = useAppSelector(state => state.sam.socialNetWork.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="socialNetWorkDetailsHeading">
          <Translate contentKey="samApp.samSocialNetWork.detail.title">SocialNetWork</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{socialNetWorkEntity.id}</dd>
          <dt>
            <span id="teacherId">
              <Translate contentKey="samApp.samSocialNetWork.teacherId">Teacher Id</Translate>
            </span>
          </dt>
          <dd>{socialNetWorkEntity.teacherId}</dd>
          <dt>
            <span id="accessToken">
              <Translate contentKey="samApp.samSocialNetWork.accessToken">Access Token</Translate>
            </span>
          </dt>
          <dd>{socialNetWorkEntity.accessToken}</dd>
          <dt>
            <span id="expiredTime">
              <Translate contentKey="samApp.samSocialNetWork.expiredTime">Expired Time</Translate>
            </span>
          </dt>
          <dd>
            {socialNetWorkEntity.expiredTime ? (
              <TextFormat value={socialNetWorkEntity.expiredTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="samApp.samSocialNetWork.type">Type</Translate>
            </span>
          </dt>
          <dd>{socialNetWorkEntity.type}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="samApp.samSocialNetWork.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {socialNetWorkEntity.createdAt ? (
              <TextFormat value={socialNetWorkEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="samApp.samSocialNetWork.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {socialNetWorkEntity.updatedAt ? (
              <TextFormat value={socialNetWorkEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/sam/social-net-work" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sam/social-net-work/${socialNetWorkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SocialNetWorkDetail;
