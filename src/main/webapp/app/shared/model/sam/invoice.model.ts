import dayjs from 'dayjs';
import { DepositStatus } from 'app/shared/model/enumerations/deposit-status.model';

export interface IInvoice {
  id?: number;
  studentId?: number;
  lessonId?: number;
  transferId?: number;
  status?: DepositStatus;
  createdAt?: string;
  updatedAt?: string;
}

export const defaultValue: Readonly<IInvoice> = {};
