import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: '589fcc68-2ddc-4ceb-934b-2ca914347f04',
  title: 'Nakfa migration SAS',
  content: 'RSS SQL services',
  rating: 57922,
};

export const sampleWithPartialData: IReview = {
  id: '862f5c02-df69-4609-b22f-06a2d8be3310',
  title: 'Saint Frozen',
  content: 'bandwidth fuchsia payment',
  rating: 79520,
  lastModifiedBy: 'bluetooth coherent Flats',
  lastModifiedDate: dayjs('2023-04-07T18:30'),
  userId: 85346,
};

export const sampleWithFullData: IReview = {
  id: 'cdce329d-1ddc-4bbd-aba4-cc4b682cba4b',
  title: 'enterprise Alabama Account',
  content: 'Checking killer',
  rating: 92443,
  createdBy: 'Fresh',
  createdDate: dayjs('2023-04-08T01:35'),
  lastModifiedBy: 'Strategist Loan',
  lastModifiedDate: dayjs('2023-04-08T01:17'),
  userId: 64148,
};

export const sampleWithNewData: NewReview = {
  title: 'Courts Territory Montserrat',
  content: 'hacking Borders Gorgeous',
  rating: 83555,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
