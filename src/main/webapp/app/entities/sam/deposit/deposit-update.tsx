import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDeposit } from 'app/shared/model/sam/deposit.model';
import { DepositStatus } from 'app/shared/model/enumerations/deposit-status.model';
import { getEntity, updateEntity, createEntity, reset } from './deposit.reducer';

export const DepositUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const depositEntity = useAppSelector(state => state.sam.deposit.entity);
  const loading = useAppSelector(state => state.sam.deposit.loading);
  const updating = useAppSelector(state => state.sam.deposit.updating);
  const updateSuccess = useAppSelector(state => state.sam.deposit.updateSuccess);
  const depositStatusValues = Object.keys(DepositStatus);
  const handleClose = () => {
    props.history.push('/sam/deposit' + props.location.search);
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
      ...depositEntity,
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
          ...depositEntity,
          createdAt: convertDateTimeFromServer(depositEntity.createdAt),
          updatedAt: convertDateTimeFromServer(depositEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="samApp.samDeposit.home.createOrEditLabel" data-cy="DepositCreateUpdateHeading">
            <Translate contentKey="samApp.samDeposit.home.createOrEditLabel">Create or edit a Deposit</Translate>
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
                  id="deposit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('samApp.samDeposit.studentId')}
                id="deposit-studentId"
                name="studentId"
                data-cy="studentId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('samApp.samDeposit.transferId')}
                id="deposit-transferId"
                name="transferId"
                data-cy="transferId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('samApp.samDeposit.status')}
                id="deposit-status"
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
                label={translate('samApp.samDeposit.createdAt')}
                id="deposit-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('samApp.samDeposit.updatedAt')}
                id="deposit-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sam/deposit" replace color="info">
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

export default DepositUpdate;
