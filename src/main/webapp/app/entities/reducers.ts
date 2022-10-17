import report from 'app/entities/sam/report/report.reducer';
import socialNetWork from 'app/entities/sam/social-net-work/social-net-work.reducer';
import invoice from 'app/entities/sam/invoice/invoice.reducer';
import customer from 'app/entities/sam/customer/customer.reducer';
import lesson from 'app/entities/sam/lesson/lesson.reducer';
import room from 'app/entities/sam/room/room.reducer';
import deposit from 'app/entities/sam/deposit/deposit.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  report,
  socialNetWork,
  invoice,
  customer,
  lesson,
  room,
  deposit,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
