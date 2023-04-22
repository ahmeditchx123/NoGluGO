import dayjs from 'dayjs/esm';

import { Diseas } from 'app/entities/enumerations/diseas.model';

import { IGlutenProfile, NewGlutenProfile } from './gluten-profile.model';

export const sampleWithRequiredData: IGlutenProfile = {
  id: '4b98526c-f277-4d90-b755-1190535266d8',
};

export const sampleWithPartialData: IGlutenProfile = {
  id: '147da80f-8ff2-4951-99d8-ce7a738a814b',
  diseas: Diseas['GLUTEN_INTOLERENCE'],
  ketoPreferenceLvl: 73554,
  createdBy: 'Virtual navigate',
  createdDate: dayjs('2023-04-08T08:00'),
  lastModifiedDate: dayjs('2023-04-08T00:50'),
  userId: 34099,
};

export const sampleWithFullData: IGlutenProfile = {
  id: '8bb1ec86-d8ac-4575-827a-eb6438649eea',
  diseas: Diseas['OTHER'],
  otherDiseas: 'functionalities set Brand',
  strictnessLevel: 80048,
  diaryFreePreferenceLvl: 76227,
  veganPreferenceLvl: 73224,
  ketoPreferenceLvl: 24281,
  createdBy: 'niches Fish',
  createdDate: dayjs('2023-04-07T17:25'),
  lastModifiedBy: 'RSS Maryland Health',
  lastModifiedDate: dayjs('2023-04-07T13:07'),
  userId: 67373,
};

export const sampleWithNewData: NewGlutenProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
