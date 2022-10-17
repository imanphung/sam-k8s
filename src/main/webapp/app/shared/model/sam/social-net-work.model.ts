import dayjs from 'dayjs';
import { TypeOfSocial } from 'app/shared/model/enumerations/type-of-social.model';

export interface ISocialNetWork {
  id?: number;
  teacherId?: number;
  accessToken?: string;
  expiredTime?: string;
  type?: TypeOfSocial;
  createdAt?: string;
  updatedAt?: string;
}

export const defaultValue: Readonly<ISocialNetWork> = {};
