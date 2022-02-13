package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


public class PriceHistory implements Serializable {
    private BigDecimal price;
    private LocalDate startDate;

    public PriceHistory(BigDecimal price, LocalDate startDate) {
        this.price = price;
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
