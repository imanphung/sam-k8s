import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const EntitiesMenu = () => {
  const lastChange = useAppSelector(state => state.locale.lastChange);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(addTranslationSourcePrefix('services/sam/'));
  }, [lastChange]);

  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/sam/report">
        <Translate contentKey="global.menu.entities.samReport" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sam/social-net-work">
        <Translate contentKey="global.menu.entities.samSocialNetWork" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sam/invoice">
        <Translate contentKey="global.menu.entities.samInvoice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sam/customer">
        <Translate contentKey="global.menu.entities.samCustomer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sam/lesson">
        <Translate contentKey="global.menu.entities.samLesson" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sam/room">
        <Translate contentKey="global.menu.entities.samRoom" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sam/deposit">
        <Translate contentKey="global.menu.entities.samDeposit" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
