import dayjs from 'dayjs';
import { DepositStatus } from 'app/shared/model/enumerations/deposit-status.model';

export interface IDeposit {
  id?: number;
  studentId?: number;
  transferId?: number;
  status?: DepositStatus;
  createdAt?: string;
  updatedAt?: string;
}

export const defaultValue: Readonly<IDeposit> = {};
