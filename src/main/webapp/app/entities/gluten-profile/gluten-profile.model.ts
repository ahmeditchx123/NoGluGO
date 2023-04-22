import dayjs from 'dayjs/esm';
import { Diseas } from 'app/entities/enumerations/diseas.model';

export interface IGlutenProfile {
  id: string;
  diseas?: Diseas | null;
  otherDiseas?: string | null;
  strictnessLevel?: number | null;
  diaryFreePreferenceLvl?: number | null;
  veganPreferenceLvl?: number | null;
  ketoPreferenceLvl?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  userId?: number | null;
}

export type NewGlutenProfile = Omit<IGlutenProfile, 'id'> & { id: null };
