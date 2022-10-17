import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInvoice } from 'app/shared/model/sam/invoice.model';
import { DepositStatus } from 'app/shared/model/enumerations/deposit-status.model';
import { getEntity, updateEntity, createEntity, reset } from './invoice.reducer';

export const InvoiceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const invoiceEntity = useAppSelector(state => state.sam.invoice.entity);
  const loading = useAppSelector(state => state.sam.invoice.loading);
  const updating = useAppSelector(state => state.sam.invoice.updating);
  const updateSuccess = useAppSelector(state => state.sam.invoice.updateSuccess);
  const depositStatusValues = Object.keys(DepositStatus);
  const handleClose = () => {
    props.history.push('/sam/invoice' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...invoiceEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          status: 'CREATED',
          ...invoiceEntity,
          createdAt: convertDateTimeFromServer(invoiceEntity.createdAt),
          updatedAt: convertDateTimeFromServer(invoiceEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="samApp.samInvoice.home.createOrEditLabel" data-cy="InvoiceCreateUpdateHeading">
            <Translate contentKey="samApp.samInvoice.home.createOrEditLabel">Create or edit a Invoice</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="invoice-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('samApp.samInvoice.studentId')}
                id="invoice-studentId"
                name="studentId"
                data-cy="studentId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('samApp.samInvoice.lessonId')}
                id="invoice-lessonId"
                name="lessonId"
                data-cy="lessonId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('samApp.samInvoice.transferId')}
                id="invoice-transferId"
                name="transferId"
                data-cy="transferId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('samApp.samInvoice.status')}
                id="invoice-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {depositStatusValues.map(depositStatus => (
                  <option value={depositStatus} key={depositStatus}>
                    {translate('samApp.DepositStatus.' + depositStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('samApp.samInvoice.createdAt')}
                id="invoice-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('samApp.samInvoice.updatedAt')}
                id="invoice-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sam/invoice" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default InvoiceUpdate;
