/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Vihanga
 */
@Entity
@Table(name = "product")
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
    @NamedQuery(name = "Product.findByProduct", query = "SELECT p FROM Product p WHERE p.product = :product"),
    @NamedQuery(name = "Product.findBySalePrice", query = "SELECT p FROM Product p WHERE p.salePrice = :salePrice"),
    @NamedQuery(name = "Product.findByCostPrice", query = "SELECT p FROM Product p WHERE p.costPrice = :costPrice"),
    @NamedQuery(name = "Product.findByDiscount", query = "SELECT p FROM Product p WHERE p.discount = :discount"),
    @NamedQuery(name = "Product.findByMeasure", query = "SELECT p FROM Product p WHERE p.measure = :measure"),
    @NamedQuery(name = "Product.findByBarCode", query = "SELECT p FROM Product p WHERE p.barCode = :barCode")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "product")
    private String product;
    @Basic(optional = false)
    @Column(name = "sale_price")
    private double salePrice;
    @Basic(optional = false)
    @Column(name = "cost_price")
    private double costPrice;
    @Basic(optional = false)
    @Column(name = "discount")
    private double discount;
    @Basic(optional = false)
    @Column(name = "measure")
    private float measure;
    @Basic(optional = false)
    @Column(name = "bar_code")
    private String barCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<ProductDistributeItem> productDistributeItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<StockAdjustmentItem> stockAdjustmentItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<RefundItem> refundItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentProduct")
    private Collection<Costing> costingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "childProduct")
    private Collection<Costing> costingCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<SupplierDamageReturnItem> supplierDamageReturnItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<Stock> stockCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<SupplierOrderItem> supplierOrderItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<GrnItem> grnItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<InvoiceItem> invoiceItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<ProductHasProductType> productHasProductTypeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referenceId")
    private Collection<ProductHasProductType> productHasProductTypeCollection1;
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Brand brandId;
    @JoinColumn(name = "product_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProductStatus productStatusId;
    @JoinColumn(name = "product_unit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProductUnit productUnitId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<DamageItem> damageItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<Store> storeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<LocationReturnItem> locationReturnItemCollection;

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, String product, double salePrice, double costPrice, double discount, float measure, String barCode) {
        this.id = id;
        this.product = product;
        this.salePrice = salePrice;
        this.costPrice = costPrice;
        this.discount = discount;
        this.measure = measure;
        this.barCode = barCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public float getMeasure() {
        return measure;
    }

    public void setMeasure(float measure) {
        this.measure = measure;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Collection<ProductDistributeItem> getProductDistributeItemCollection() {
        return productDistributeItemCollection;
    }

    public void setProductDistributeItemCollection(Collection<ProductDistributeItem> productDistributeItemCollection) {
        this.productDistributeItemCollection = productDistributeItemCollection;
    }

    public Collection<StockAdjustmentItem> getStockAdjustmentItemCollection() {
        return stockAdjustmentItemCollection;
    }

    public void setStockAdjustmentItemCollection(Collection<StockAdjustmentItem> stockAdjustmentItemCollection) {
        this.stockAdjustmentItemCollection = stockAdjustmentItemCollection;
    }

    public Collection<RefundItem> getRefundItemCollection() {
        return refundItemCollection;
    }

    public void setRefundItemCollection(Collection<RefundItem> refundItemCollection) {
        this.refundItemCollection = refundItemCollection;
    }

    public Collection<Costing> getCostingCollection() {
        return costingCollection;
    }

    public void setCostingCollection(Collection<Costing> costingCollection) {
        this.costingCollection = costingCollection;
    }

    public Collection<Costing> getCostingCollection1() {
        return costingCollection1;
    }

    public void setCostingCollection1(Collection<Costing> costingCollection1) {
        this.costingCollection1 = costingCollection1;
    }

    public Collection<SupplierDamageReturnItem> getSupplierDamageReturnItemCollection() {
        return supplierDamageReturnItemCollection;
    }

    public void setSupplierDamageReturnItemCollection(Collection<SupplierDamageReturnItem> supplierDamageReturnItemCollection) {
        this.supplierDamageReturnItemCollection = supplierDamageReturnItemCollection;
    }

    public Collection<Stock> getStockCollection() {
        return stockCollection;
    }

    public void setStockCollection(Collection<Stock> stockCollection) {
        this.stockCollection = stockCollection;
    }

    public Collection<SupplierOrderItem> getSupplierOrderItemCollection() {
        return supplierOrderItemCollection;
    }

    public void setSupplierOrderItemCollection(Collection<SupplierOrderItem> supplierOrderItemCollection) {
        this.supplierOrderItemCollection = supplierOrderItemCollection;
    }

    public Collection<GrnItem> getGrnItemCollection() {
        return grnItemCollection;
    }

    public void setGrnItemCollection(Collection<GrnItem> grnItemCollection) {
        this.grnItemCollection = grnItemCollection;
    }

    public Collection<InvoiceItem> getInvoiceItemCollection() {
        return invoiceItemCollection;
    }

    public void setInvoiceItemCollection(Collection<InvoiceItem> invoiceItemCollection) {
        this.invoiceItemCollection = invoiceItemCollection;
    }

    public Collection<ProductHasProductType> getProductHasProductTypeCollection() {
        return productHasProductTypeCollection;
    }

    public void setProductHasProductTypeCollection(Collection<ProductHasProductType> productHasProductTypeCollection) {
        this.productHasProductTypeCollection = productHasProductTypeCollection;
    }

    public Collection<ProductHasProductType> getProductHasProductTypeCollection1() {
        return productHasProductTypeCollection1;
    }

    public void setProductHasProductTypeCollection1(Collection<ProductHasProductType> productHasProductTypeCollection1) {
        this.productHasProductTypeCollection1 = productHasProductTypeCollection1;
    }

    public Brand getBrandId() {
        return brandId;
    }

    public void setBrandId(Brand brandId) {
        this.brandId = brandId;
    }

    public ProductStatus getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(ProductStatus productStatusId) {
        this.productStatusId = productStatusId;
    }

    public ProductUnit getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(ProductUnit productUnitId) {
        this.productUnitId = productUnitId;
    }

    public Collection<DamageItem> getDamageItemCollection() {
        return damageItemCollection;
    }

    public void setDamageItemCollection(Collection<DamageItem> damageItemCollection) {
        this.damageItemCollection = damageItemCollection;
    }

    public Collection<Store> getStoreCollection() {
        return storeCollection;
    }

    public void setStoreCollection(Collection<Store> storeCollection) {
        this.storeCollection = storeCollection;
    }

    public Collection<LocationReturnItem> getLocationReturnItemCollection() {
        return locationReturnItemCollection;
    }

    public void setLocationReturnItemCollection(Collection<LocationReturnItem> locationReturnItemCollection) {
        this.locationReturnItemCollection = locationReturnItemCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Product[ id=" + id + " ]";
    }
    
}
