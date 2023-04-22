import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 'c4f66c59-0ae8-4960-96be-a072eb974882',
  content: 'generating Row calculate',
};

export const sampleWithPartialData: IComment = {
  id: '80d4a1ed-15d9-445f-a300-eba4b51b3dd2',
  content: 'purple magnetic Brand',
  createdBy: 'Soft port',
  lastModifiedBy: 'Keyboard',
  userId: 97587,
};

export const sampleWithFullData: IComment = {
  id: '8f15fec7-067e-4b58-9a9f-bb7da4aea19f',
  content: 'Internal systemic Cameroon',
  createdBy: 'regional drive',
  createdDate: dayjs('2023-04-07T14:46'),
  lastModifiedBy: 'bypass',
  lastModifiedDate: dayjs('2023-04-08T00:59'),
  userId: 86806,
};

export const sampleWithNewData: NewComment = {
  content: 'Unbranded Market',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
