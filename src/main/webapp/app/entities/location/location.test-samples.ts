import dayjs from 'dayjs/esm';

import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 'e0df5e1a-be08-465b-acc9-eb918c1e21ed',
  lattitude: 40398,
  longtitude: 4662,
};

export const sampleWithPartialData: ILocation = {
  id: '5dc62249-b200-4631-80b1-a55724e58dff',
  lattitude: 79275,
  longtitude: 43970,
  lastModifiedBy: 'JBOD Salad Books',
};

export const sampleWithFullData: ILocation = {
  id: 'bead30f7-dcc0-4f2b-9819-34fccaf8a279',
  lattitude: 1750,
  longtitude: 78563,
  createdBy: 'application Officer Soft',
  createdDate: dayjs('2023-04-08T03:53'),
  lastModifiedBy: 'Specialist',
  lastModifiedDate: dayjs('2023-04-07T19:15'),
};

export const sampleWithNewData: NewLocation = {
  lattitude: 14294,
  longtitude: 47437,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
