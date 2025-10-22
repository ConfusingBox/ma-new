package com.example.eventplanner.model;

import com.example.eventplanner.model.enums.ProductStatus;
import com.example.eventplanner.model.enums.ServiceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("serviceAndProductCategory")
    @Expose
    private ServiceAndProductCategory serviceAndProductCategory;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("serviceAndProductProvider")
    @Expose
    private ServiceAndProductProvider serviceAndProductProvider;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("productPictures")
    @Expose
    private String[] productPictures;

    @SerializedName("status")
    @Expose
    private ProductStatus status;

    @SerializedName("eventTypes")
    @Expose
    private String eventTypes;

    public Product() {
    }

    public Product(Integer id, ServiceAndProductCategory serviceAndProductCategory, String price, ServiceAndProductProvider serviceAndProductProvider, String name, String description, String discount, String[] productPictures, ProductStatus status, String eventTypes) {
        this.id = id;
        this.serviceAndProductCategory = serviceAndProductCategory;
        this.price = price;
        this.serviceAndProductProvider = serviceAndProductProvider;
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.productPictures = productPictures;
        this.status = status;
        this.eventTypes = eventTypes;
    }

    public ServiceAndProductProvider getServiceAndProductProvider() {
        return serviceAndProductProvider;
    }

    public void setServiceAndProductProvider(ServiceAndProductProvider serviceAndProductProvider) {
        this.serviceAndProductProvider = serviceAndProductProvider;
    }

    public String[] getProductPictures() {
        return productPictures;
    }

    public void setProductPictures(String[] productPictures) {
        this.productPictures = productPictures;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceAndProductCategory getServiceAndProductCategory() {
        return serviceAndProductCategory;
    }

    public void setServiceAndProductCategory(ServiceAndProductCategory serviceAndProductCategory) {
        this.serviceAndProductCategory = serviceAndProductCategory;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(String eventTypes) {
        this.eventTypes = eventTypes;
    }
}
