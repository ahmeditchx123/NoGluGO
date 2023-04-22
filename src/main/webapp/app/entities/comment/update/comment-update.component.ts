import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommentFormService, CommentFormGroup } from './comment-form.service';
import { IComment } from '../comment.model';
import { CommentService } from '../service/comment.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html',
})
export class CommentUpdateComponent implements OnInit {
  isSaving = false;
  comment: IComment | null = null;

  articlesSharedCollection: IArticle[] = [];

  editForm: CommentFormGroup = this.commentFormService.createCommentFormGroup();

  constructor(
    protected commentService: CommentService,
    protected commentFormService: CommentFormService,
    protected articleService: ArticleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareArticle = (o1: IArticle | null, o2: IArticle | null): boolean => this.articleService.compareArticle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.comment = comment;
      if (comment) {
        this.updateForm(comment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comment = this.commentFormService.getComment(this.editForm);
    if (comment.id !== null) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(comment: IComment): void {
    this.comment = comment;
    this.commentFormService.resetForm(this.editForm, comment);

    this.articlesSharedCollection = this.articleService.addArticleToCollectionIfMissing<IArticle>(
      this.articlesSharedCollection,
      comment.article
    );
  }

  protected loadRelationshipsOptions(): void {
    this.articleService
      .query()
      .pipe(map((res: HttpResponse<IArticle[]>) => res.body ?? []))
      .pipe(map((articles: IArticle[]) => this.articleService.addArticleToCollectionIfMissing<IArticle>(articles, this.comment?.article)))
      .subscribe((articles: IArticle[]) => (this.articlesSharedCollection = articles));
  }
}
