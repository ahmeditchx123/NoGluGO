import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommentFormService } from './comment-form.service';
import { CommentService } from '../service/comment.service';
import { IComment } from '../comment.model';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';

import { CommentUpdateComponent } from './comment-update.component';

describe('Comment Management Update Component', () => {
  let comp: CommentUpdateComponent;
  let fixture: ComponentFixture<CommentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commentFormService: CommentFormService;
  let commentService: CommentService;
  let articleService: ArticleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commentFormService = TestBed.inject(CommentFormService);
    commentService = TestBed.inject(CommentService);
    articleService = TestBed.inject(ArticleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Article query and add missing value', () => {
      const comment: IComment = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const article: IArticle = { id: '68fd2d57-8aed-4b77-8f65-72c1feb890aa' };
      comment.article = article;

      const articleCollection: IArticle[] = [{ id: '476bdc18-5567-41dd-a919-5c0a0c4d7ef5' }];
      jest.spyOn(articleService, 'query').mockReturnValue(of(new HttpResponse({ body: articleCollection })));
      const additionalArticles = [article];
      const expectedCollection: IArticle[] = [...additionalArticles, ...articleCollection];
      jest.spyOn(articleService, 'addArticleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      expect(articleService.query).toHaveBeenCalled();
      expect(articleService.addArticleToCollectionIfMissing).toHaveBeenCalledWith(
        articleCollection,
        ...additionalArticles.map(expect.objectContaining)
      );
      expect(comp.articlesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const comment: IComment = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const article: IArticle = { id: '92b92224-868c-4916-a4e3-997a7d17549d' };
      comment.article = article;

      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      expect(comp.articlesSharedCollection).toContain(article);
      expect(comp.comment).toEqual(comment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComment>>();
      const comment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(commentFormService, 'getComment').mockReturnValue(comment);
      jest.spyOn(commentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comment }));
      saveSubject.complete();

      // THEN
      expect(commentFormService.getComment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commentService.update).toHaveBeenCalledWith(expect.objectContaining(comment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComment>>();
      const comment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(commentFormService, 'getComment').mockReturnValue({ id: null });
      jest.spyOn(commentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comment }));
      saveSubject.complete();

      // THEN
      expect(commentFormService.getComment).toHaveBeenCalled();
      expect(commentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComment>>();
      const comment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(commentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareArticle', () => {
      it('Should forward to articleService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(articleService, 'compareArticle');
        comp.compareArticle(entity, entity2);
        expect(articleService.compareArticle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
