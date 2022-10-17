import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISocialNetWork } from 'app/shared/model/sam/social-net-work.model';
import { TypeOfSocial } from 'app/shared/model/enumerations/type-of-social.model';
import { getEntity, updateEntity, createEntity, reset } from './social-net-work.reducer';

export const SocialNetWorkUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const socialNetWorkEntity = useAppSelector(state => state.sam.socialNetWork.entity);
  const loading = useAppSelector(state => state.sam.socialNetWork.loading);
  const updating = useAppSelector(state => state.sam.socialNetWork.updating);
  const updateSuccess = useAppSelector(state => state.sam.socialNetWork.updateSuccess);
  const typeOfSocialValues = Object.keys(TypeOfSocial);
  const handleClose = () => {
    props.history.push('/sam/social-net-work');
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
    values.expiredTime = convertDateTimeToServer(values.expiredTime);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...socialNetWorkEntity,
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
          expiredTime: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          type: 'FACEBOOK',
          ...socialNetWorkEntity,
          expiredTime: convertDateTimeFromServer(socialNetWorkEntity.expiredTime),
          createdAt: convertDateTimeFromServer(socialNetWorkEntity.createdAt),
          updatedAt: convertDateTimeFromServer(socialNetWorkEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="samApp.samSocialNetWork.home.createOrEditLabel" data-cy="SocialNetWorkCreateUpdateHeading">
            <Translate contentKey="samApp.samSocialNetWork.home.createOrEditLabel">Create or edit a SocialNetWork</Translate>
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
                  id="social-net-work-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('samApp.samSocialNetWork.teacherId')}
                id="social-net-work-teacherId"
                name="teacherId"
                data-cy="teacherId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('samApp.samSocialNetWork.accessToken')}
                id="social-net-work-accessToken"
                name="accessToken"
                data-cy="accessToken"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('samApp.samSocialNetWork.expiredTime')}
                id="social-net-work-expiredTime"
                name="expiredTime"
                data-cy="expiredTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('samApp.samSocialNetWork.type')}
                id="social-net-work-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {typeOfSocialValues.map(typeOfSocial => (
                  <option value={typeOfSocial} key={typeOfSocial}>
                    {translate('samApp.TypeOfSocial.' + typeOfSocial)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('samApp.samSocialNetWork.createdAt')}
                id="social-net-work-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('samApp.samSocialNetWork.updatedAt')}
                id="social-net-work-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sam/social-net-work" replace color="info">
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

export default SocialNetWorkUpdate;
