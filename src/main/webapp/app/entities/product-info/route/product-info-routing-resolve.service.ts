import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductInfo } from '../product-info.model';
import { ProductInfoService } from '../service/product-info.service';

@Injectable({ providedIn: 'root' })
export class ProductInfoRoutingResolveService implements Resolve<IProductInfo | null> {
  constructor(protected service: ProductInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductInfo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productInfo: HttpResponse<IProductInfo>) => {
          if (productInfo.body) {
            return of(productInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
