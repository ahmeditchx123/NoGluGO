import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'store',
        data: { pageTitle: 'noGluGoApp.store.home.title' },
        loadChildren: () => import('./store/store.module').then(m => m.StoreModule),
      },
      {
        path: 'restaurant',
        data: { pageTitle: 'noGluGoApp.restaurant.home.title' },
        loadChildren: () => import('./restaurant/restaurant.module').then(m => m.RestaurantModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'noGluGoApp.address.home.title' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'noGluGoApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'noGluGoApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'product-info',
        data: { pageTitle: 'noGluGoApp.productInfo.home.title' },
        loadChildren: () => import('./product-info/product-info.module').then(m => m.ProductInfoModule),
      },
      {
        path: 'review',
        data: { pageTitle: 'noGluGoApp.review.home.title' },
        loadChildren: () => import('./review/review.module').then(m => m.ReviewModule),
      },
      {
        path: 'menu',
        data: { pageTitle: 'noGluGoApp.menu.home.title' },
        loadChildren: () => import('./menu/menu.module').then(m => m.MenuModule),
      },
      {
        path: 'menu-item',
        data: { pageTitle: 'noGluGoApp.menuItem.home.title' },
        loadChildren: () => import('./menu-item/menu-item.module').then(m => m.MenuItemModule),
      },
      {
        path: 'cart',
        data: { pageTitle: 'noGluGoApp.cart.home.title' },
        loadChildren: () => import('./cart/cart.module').then(m => m.CartModule),
      },
      {
        path: 'cart-item',
        data: { pageTitle: 'noGluGoApp.cartItem.home.title' },
        loadChildren: () => import('./cart-item/cart-item.module').then(m => m.CartItemModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'noGluGoApp.order.home.title' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'noGluGoApp.orderItem.home.title' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      {
        path: 'article',
        data: { pageTitle: 'noGluGoApp.article.home.title' },
        loadChildren: () => import('./article/article.module').then(m => m.ArticleModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'noGluGoApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'gluten-profile',
        data: { pageTitle: 'noGluGoApp.glutenProfile.home.title' },
        loadChildren: () => import('./gluten-profile/gluten-profile.module').then(m => m.GlutenProfileModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
