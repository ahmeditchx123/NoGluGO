import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IArticle, NewArticle } from '../article.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticle for edit and NewArticleFormGroupInput for create.
 */
type ArticleFormGroupInput = IArticle | PartialWithRequiredKeyOf<NewArticle>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IArticle | NewArticle> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ArticleFormRawValue = FormValueOf<IArticle>;

type NewArticleFormRawValue = FormValueOf<NewArticle>;

type ArticleFormDefaults = Pick<NewArticle, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ArticleFormGroupContent = {
  id: FormControl<ArticleFormRawValue['id'] | NewArticle['id']>;
  name: FormControl<ArticleFormRawValue['name']>;
  content: FormControl<ArticleFormRawValue['content']>;
  imgPath: FormControl<ArticleFormRawValue['imgPath']>;
  createdBy: FormControl<ArticleFormRawValue['createdBy']>;
  createdDate: FormControl<ArticleFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ArticleFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ArticleFormRawValue['lastModifiedDate']>;
  userId: FormControl<ArticleFormRawValue['userId']>;
};

export type ArticleFormGroup = FormGroup<ArticleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleFormService {
  createArticleFormGroup(article: ArticleFormGroupInput = { id: null }): ArticleFormGroup {
    const articleRawValue = this.convertArticleToArticleRawValue({
      ...this.getFormDefaults(),
      ...article,
    });
    return new FormGroup<ArticleFormGroupContent>({
      id: new FormControl(
        { value: articleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(articleRawValue.name, {
        validators: [Validators.required],
      }),
      content: new FormControl(articleRawValue.content, {
        validators: [Validators.required],
      }),
      imgPath: new FormControl(articleRawValue.imgPath),
      createdBy: new FormControl(articleRawValue.createdBy),
      createdDate: new FormControl(articleRawValue.createdDate),
      lastModifiedBy: new FormControl(articleRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(articleRawValue.lastModifiedDate),
      userId: new FormControl(articleRawValue.userId),
    });
  }

  getArticle(form: ArticleFormGroup): IArticle | NewArticle {
    return this.convertArticleRawValueToArticle(form.getRawValue() as ArticleFormRawValue | NewArticleFormRawValue);
  }

  resetForm(form: ArticleFormGroup, article: ArticleFormGroupInput): void {
    const articleRawValue = this.convertArticleToArticleRawValue({ ...this.getFormDefaults(), ...article });
    form.reset(
      {
        ...articleRawValue,
        id: { value: articleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ArticleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertArticleRawValueToArticle(rawArticle: ArticleFormRawValue | NewArticleFormRawValue): IArticle | NewArticle {
    return {
      ...rawArticle,
      createdDate: dayjs(rawArticle.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawArticle.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertArticleToArticleRawValue(
    article: IArticle | (Partial<NewArticle> & ArticleFormDefaults)
  ): ArticleFormRawValue | PartialWithRequiredKeyOf<NewArticleFormRawValue> {
    return {
      ...article,
      createdDate: article.createdDate ? article.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: article.lastModifiedDate ? article.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
