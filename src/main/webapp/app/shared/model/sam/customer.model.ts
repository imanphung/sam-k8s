import dayjs from 'dayjs';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { TypeOfAccount } from 'app/shared/model/enumerations/type-of-account.model';

export interface ICustomer {
  id?: number;
  firstName?: string;
  lastName?: string;
  gender?: Gender;
  email?: string;
  phone?: string;
  address?: string;
  city?: string;
  country?: string;
  typeOf?: TypeOfAccount;
  createdAt?: string;
  updatedAt?: string;
}

export const defaultValue: Readonly<ICustomer> = {};
