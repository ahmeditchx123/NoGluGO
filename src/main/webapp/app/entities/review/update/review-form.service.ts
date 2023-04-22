import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReview, NewReview } from '../review.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReview for edit and NewReviewFormGroupInput for create.
 */
type ReviewFormGroupInput = IReview | PartialWithRequiredKeyOf<NewReview>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReview | NewReview> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ReviewFormRawValue = FormValueOf<IReview>;

type NewReviewFormRawValue = FormValueOf<NewReview>;

type ReviewFormDefaults = Pick<NewReview, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ReviewFormGroupContent = {
  id: FormControl<ReviewFormRawValue['id'] | NewReview['id']>;
  title: FormControl<ReviewFormRawValue['title']>;
  content: FormControl<ReviewFormRawValue['content']>;
  rating: FormControl<ReviewFormRawValue['rating']>;
  createdBy: FormControl<ReviewFormRawValue['createdBy']>;
  createdDate: FormControl<ReviewFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ReviewFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ReviewFormRawValue['lastModifiedDate']>;
  userId: FormControl<ReviewFormRawValue['userId']>;
  menuItem: FormControl<ReviewFormRawValue['menuItem']>;
  product: FormControl<ReviewFormRawValue['product']>;
};

export type ReviewFormGroup = FormGroup<ReviewFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReviewFormService {
  createReviewFormGroup(review: ReviewFormGroupInput = { id: null }): ReviewFormGroup {
    const reviewRawValue = this.convertReviewToReviewRawValue({
      ...this.getFormDefaults(),
      ...review,
    });
    return new FormGroup<ReviewFormGroupContent>({
      id: new FormControl(
        { value: reviewRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(reviewRawValue.title, {
        validators: [Validators.required],
      }),
      content: new FormControl(reviewRawValue.content, {
        validators: [Validators.required],
      }),
      rating: new FormControl(reviewRawValue.rating, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(reviewRawValue.createdBy),
      createdDate: new FormControl(reviewRawValue.createdDate),
      lastModifiedBy: new FormControl(reviewRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(reviewRawValue.lastModifiedDate),
      userId: new FormControl(reviewRawValue.userId),
      menuItem: new FormControl(reviewRawValue.menuItem),
      product: new FormControl(reviewRawValue.product),
    });
  }

  getReview(form: ReviewFormGroup): IReview | NewReview {
    return this.convertReviewRawValueToReview(form.getRawValue() as ReviewFormRawValue | NewReviewFormRawValue);
  }

  resetForm(form: ReviewFormGroup, review: ReviewFormGroupInput): void {
    const reviewRawValue = this.convertReviewToReviewRawValue({ ...this.getFormDefaults(), ...review });
    form.reset(
      {
        ...reviewRawValue,
        id: { value: reviewRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReviewFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertReviewRawValueToReview(rawReview: ReviewFormRawValue | NewReviewFormRawValue): IReview | NewReview {
    return {
      ...rawReview,
      createdDate: dayjs(rawReview.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawReview.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertReviewToReviewRawValue(
    review: IReview | (Partial<NewReview> & ReviewFormDefaults)
  ): ReviewFormRawValue | PartialWithRequiredKeyOf<NewReviewFormRawValue> {
    return {
      ...review,
      createdDate: review.createdDate ? review.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: review.lastModifiedDate ? review.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
