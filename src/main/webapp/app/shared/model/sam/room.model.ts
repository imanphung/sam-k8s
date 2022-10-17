import dayjs from 'dayjs';
import { RoomStatus } from 'app/shared/model/enumerations/room-status.model';

export interface IRoom {
  id?: number;
  meetingId?: number;
  createdBy?: number;
  lessonId?: number;
  startTime?: string;
  endTime?: string | null;
  status?: RoomStatus;
  createdAt?: string;
  updatedAt?: string;
}

export const defaultValue: Readonly<IRoom> = {};
