import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComment, NewComment } from '../comment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IComment for edit and NewCommentFormGroupInput for create.
 */
type CommentFormGroupInput = IComment | PartialWithRequiredKeyOf<NewComment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IComment | NewComment> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type CommentFormRawValue = FormValueOf<IComment>;

type NewCommentFormRawValue = FormValueOf<NewComment>;

type CommentFormDefaults = Pick<NewComment, 'id' | 'createdDate' | 'lastModifiedDate'>;

type CommentFormGroupContent = {
  id: FormControl<CommentFormRawValue['id'] | NewComment['id']>;
  content: FormControl<CommentFormRawValue['content']>;
  createdBy: FormControl<CommentFormRawValue['createdBy']>;
  createdDate: FormControl<CommentFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<CommentFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<CommentFormRawValue['lastModifiedDate']>;
  userId: FormControl<CommentFormRawValue['userId']>;
  article: FormControl<CommentFormRawValue['article']>;
};

export type CommentFormGroup = FormGroup<CommentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommentFormService {
  createCommentFormGroup(comment: CommentFormGroupInput = { id: null }): CommentFormGroup {
    const commentRawValue = this.convertCommentToCommentRawValue({
      ...this.getFormDefaults(),
      ...comment,
    });
    return new FormGroup<CommentFormGroupContent>({
      id: new FormControl(
        { value: commentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      content: new FormControl(commentRawValue.content, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(commentRawValue.createdBy),
      createdDate: new FormControl(commentRawValue.createdDate),
      lastModifiedBy: new FormControl(commentRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(commentRawValue.lastModifiedDate),
      userId: new FormControl(commentRawValue.userId),
      article: new FormControl(commentRawValue.article),
    });
  }

  getComment(form: CommentFormGroup): IComment | NewComment {
    return this.convertCommentRawValueToComment(form.getRawValue() as CommentFormRawValue | NewCommentFormRawValue);
  }

  resetForm(form: CommentFormGroup, comment: CommentFormGroupInput): void {
    const commentRawValue = this.convertCommentToCommentRawValue({ ...this.getFormDefaults(), ...comment });
    form.reset(
      {
        ...commentRawValue,
        id: { value: commentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CommentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertCommentRawValueToComment(rawComment: CommentFormRawValue | NewCommentFormRawValue): IComment | NewComment {
    return {
      ...rawComment,
      createdDate: dayjs(rawComment.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawComment.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCommentToCommentRawValue(
    comment: IComment | (Partial<NewComment> & CommentFormDefaults)
  ): CommentFormRawValue | PartialWithRequiredKeyOf<NewCommentFormRawValue> {
    return {
      ...comment,
      createdDate: comment.createdDate ? comment.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: comment.lastModifiedDate ? comment.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
