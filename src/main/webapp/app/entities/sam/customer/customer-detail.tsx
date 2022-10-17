import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer.reducer';

export const CustomerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const customerEntity = useAppSelector(state => state.sam.customer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="samApp.samCustomer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="samApp.samCustomer.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="samApp.samCustomer.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.lastName}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="samApp.samCustomer.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{customerEntity.gender}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="samApp.samCustomer.email">Email</Translate>
            </span>
          </dt>
          <dd>{customerEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="samApp.samCustomer.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{customerEntity.phone}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="samApp.samCustomer.address">Address</Translate>
            </span>
          </dt>
          <dd>{customerEntity.address}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="samApp.samCustomer.city">City</Translate>
            </span>
          </dt>
          <dd>{customerEntity.city}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="samApp.samCustomer.country">Country</Translate>
            </span>
          </dt>
          <dd>{customerEntity.country}</dd>
          <dt>
            <span id="typeOf">
              <Translate contentKey="samApp.samCustomer.typeOf">Type Of</Translate>
            </span>
          </dt>
          <dd>{customerEntity.typeOf}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="samApp.samCustomer.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{customerEntity.createdAt ? <TextFormat value={customerEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="samApp.samCustomer.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{customerEntity.updatedAt ? <TextFormat value={customerEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/sam/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sam/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetail;
