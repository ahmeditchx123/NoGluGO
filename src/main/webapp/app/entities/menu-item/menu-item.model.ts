import dayjs from 'dayjs/esm';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IMenuItem {
  id: string;
  name?: string | null;
  content?: string | null;
  imgPath?: string | null;
  unitPrice?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  menu?: Pick<IMenu, 'id'> | null;
}

export type NewMenuItem = Omit<IMenuItem, 'id'> & { id: null };
