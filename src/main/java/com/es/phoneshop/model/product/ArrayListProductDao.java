package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


public class ArrayListProductDao implements ProductDao {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();
    private List<Product> products;
    private Long maxId=0L;



    public ArrayListProductDao() {
        this.products = new ArrayList<>();
        saveSampleProducts();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundExeption {
        readLock.lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(ProductNotFoundExeption::new);
        }finally {
            readLock.unlock();
        }

    }

    @Override
    public List<Product> findProducts() {
        readLock.lock();
        try {
            return products.stream()
                    .filter(this::productHasPrice)
                    .filter(this::productIsInStock)
                    .collect(Collectors.toList());
        }finally {
            readLock.unlock();
        }
    }

    private boolean productIsInStock(Product product){
        return product.getStock()>0;
    }

    private boolean productHasPrice(Product product){
        return product.getPrice()!=null;
    }

    @Override
    public void save(Product product) throws IncorrectIdExeption {

        writeLock.lock();
        try {
            if(product.getId()==null){
                product.setId(maxId++);
                products.add(product);
            } else {
                Optional<Product> productForUpdate= products.stream()
                        .filter(currentProduct ->product.getId().equals(currentProduct.getId()))
                        .findAny();
                if(productForUpdate.isPresent()){
                    products.set(products.indexOf(productForUpdate.get()),product);
                }else {
                    throw  new IncorrectIdExeption();
                }
            }
        }
        finally {
            writeLock.unlock();

        }

    }

    @Override
    public void delete(Long id) throws ProductNotFoundExeption {
        writeLock.lock();
        try {
            Product productToRemove=products.stream()
                    .filter(product ->id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(ProductNotFoundExeption::new);
            products.remove(productToRemove);
        }finally {
            writeLock.unlock();
        }

    }


    private void saveSampleProducts(){
        Currency usd = Currency.getInstance("USD");
        save(new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product( "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product( "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product( "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product( "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product( "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product( "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product( "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product( "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product( "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product( "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product( "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product( "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}
