import dayjs from 'dayjs';
import { TypeOfStar } from 'app/shared/model/enumerations/type-of-star.model';

export interface IReport {
  id?: number;
  studentId?: number;
  lessonId?: number;
  comment?: string | null;
  star?: TypeOfStar;
  createdAt?: string;
  updatedAt?: string;
}

export const defaultValue: Readonly<IReport> = {};
