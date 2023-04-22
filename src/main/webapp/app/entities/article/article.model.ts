import dayjs from 'dayjs/esm';

export interface IArticle {
  id: string;
  name?: string | null;
  content?: string | null;
  imgPath?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  userId?: number | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
