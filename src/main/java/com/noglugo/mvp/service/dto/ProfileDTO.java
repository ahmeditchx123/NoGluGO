package com.noglugo.mvp.service.dto;

import java.io.Serializable;
import java.util.List;

public class ProfileDTO implements Serializable {

    private AdminUserDTO user;
    private StoreDTO store;
    private AddressDTO address;
    private CartDTO cart;
    private List<OrderDTO> orders;

    public ProfileDTO(AdminUserDTO user, StoreDTO store, AddressDTO address, CartDTO cart, List<OrderDTO> orders) {
        this.user = user;
        this.store = store;
        this.address = address;
        this.cart = cart;
        this.orders = orders;
    }

    public ProfileDTO() {}

    public AdminUserDTO getUser() {
        return user;
    }

    public void setUser(AdminUserDTO user) {
        this.user = user;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public CartDTO getCart() {
        return cart;
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }
}
