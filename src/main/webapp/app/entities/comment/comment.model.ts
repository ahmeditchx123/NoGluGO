import dayjs from 'dayjs/esm';
import { IArticle } from 'app/entities/article/article.model';

export interface IComment {
  id: string;
  content?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  userId?: number | null;
  article?: Pick<IArticle, 'id'> | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
