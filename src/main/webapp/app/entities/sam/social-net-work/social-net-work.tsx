import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISocialNetWork } from 'app/shared/model/sam/social-net-work.model';
import { getEntities } from './social-net-work.reducer';

export const SocialNetWork = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const socialNetWorkList = useAppSelector(state => state.sam.socialNetWork.entities);
  const loading = useAppSelector(state => state.sam.socialNetWork.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="social-net-work-heading" data-cy="SocialNetWorkHeading">
        <Translate contentKey="samApp.samSocialNetWork.home.title">Social Net Works</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="samApp.samSocialNetWork.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/sam/social-net-work/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="samApp.samSocialNetWork.home.createLabel">Create new Social Net Work</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {socialNetWorkList && socialNetWorkList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="samApp.samSocialNetWork.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="samApp.samSocialNetWork.teacherId">Teacher Id</Translate>
                </th>
                <th>
                  <Translate contentKey="samApp.samSocialNetWork.accessToken">Access Token</Translate>
                </th>
                <th>
                  <Translate contentKey="samApp.samSocialNetWork.expiredTime">Expired Time</Translate>
                </th>
                <th>
                  <Translate contentKey="samApp.samSocialNetWork.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="samApp.samSocialNetWork.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="samApp.samSocialNetWork.updatedAt">Updated At</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {socialNetWorkList.map((socialNetWork, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/sam/social-net-work/${socialNetWork.id}`} color="link" size="sm">
                      {socialNetWork.id}
                    </Button>
                  </td>
                  <td>{socialNetWork.teacherId}</td>
                  <td>{socialNetWork.accessToken}</td>
                  <td>
                    {socialNetWork.expiredTime ? (
                      <TextFormat type="date" value={socialNetWork.expiredTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`samApp.TypeOfSocial.${socialNetWork.type}`} />
                  </td>
                  <td>
                    {socialNetWork.createdAt ? <TextFormat type="date" value={socialNetWork.createdAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {socialNetWork.updatedAt ? <TextFormat type="date" value={socialNetWork.updatedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/sam/social-net-work/${socialNetWork.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/sam/social-net-work/${socialNetWork.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/sam/social-net-work/${socialNetWork.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="samApp.samSocialNetWork.home.notFound">No Social Net Works found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SocialNetWork;
