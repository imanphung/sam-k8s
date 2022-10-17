import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './invoice.reducer';

export const InvoiceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const invoiceEntity = useAppSelector(state => state.sam.invoice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="invoiceDetailsHeading">
          <Translate contentKey="samApp.samInvoice.detail.title">Invoice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.id}</dd>
          <dt>
            <span id="studentId">
              <Translate contentKey="samApp.samInvoice.studentId">Student Id</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.studentId}</dd>
          <dt>
            <span id="lessonId">
              <Translate contentKey="samApp.samInvoice.lessonId">Lesson Id</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.lessonId}</dd>
          <dt>
            <span id="transferId">
              <Translate contentKey="samApp.samInvoice.transferId">Transfer Id</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.transferId}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="samApp.samInvoice.status">Status</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="samApp.samInvoice.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.createdAt ? <TextFormat value={invoiceEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="samApp.samInvoice.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.updatedAt ? <TextFormat value={invoiceEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/sam/invoice" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sam/invoice/${invoiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InvoiceDetail;
