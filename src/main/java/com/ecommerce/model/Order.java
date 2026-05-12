package com.ecommerce.model;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
public class Order {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.ENGLISH)
                             .withZone(ZoneId.systemDefault());
    private long id;
    private long userId;
    private long productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String notes;
    private String status;
    private Instant createdAt;
    public Order() {}
    public long getId()                  { return id; }
    public void setId(long id)           { this.id = id; }
    public long getUserId()              { return userId; }
    public void setUserId(long userId)   { this.userId = userId; }
    public long getProductId()           { return productId; }
    public void setProductId(long pid)   { this.productId = pid; }
    public String getProductName()       { return productName; }
    public void setProductName(String n) { this.productName = n; }
    public int getQuantity()             { return quantity; }
    public void setQuantity(int q)       { this.quantity = q; }
    public BigDecimal getUnitPrice()     { return unitPrice; }
    public void setUnitPrice(BigDecimal p){ this.unitPrice = p; }
    public BigDecimal getTotalPrice()    { return totalPrice; }
    public void setTotalPrice(BigDecimal t){ this.totalPrice = t; }
    public String getFullName()          { return fullName; }
    public void setFullName(String n)    { this.fullName = n; }
    public String getPhone()             { return phone; }
    public void setPhone(String p)       { this.phone = p; }
    public String getAddress()           { return address; }
    public void setAddress(String a)     { this.address = a; }
    public String getCity()              { return city; }
    public void setCity(String c)        { this.city = c; }
    public String getNotes()             { return notes; }
    public void setNotes(String n)       { this.notes = n; }
    public String getStatus()            { return status; }
    public void setStatus(String s)      { this.status = s; }
    public Instant getCreatedAt()        { return createdAt; }
    public void setCreatedAt(Instant t)  { this.createdAt = t; }
    public String getCreatedAtFormatted() {
        return createdAt == null ? "" : FMT.format(createdAt);
    }
}
