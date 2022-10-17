import dayjs from 'dayjs';

export interface ILesson {
  id?: number;
  teacherId?: number;
  title?: string;
  content?: string;
  price?: number;
  time?: number;
  createdAt?: string;
  updatedAt?: string;
}

export const defaultValue: Readonly<ILesson> = {};
