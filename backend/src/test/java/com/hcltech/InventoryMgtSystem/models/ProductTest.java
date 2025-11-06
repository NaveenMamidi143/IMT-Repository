package com.hcltech.InventoryMgtSystem.models;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductFields() {
        Category category = new Category();
        LocalDateTime expiry = LocalDateTime.of(2025, 12, 31, 23, 59);

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setSku("SKU123");
        product.setPrice(new BigDecimal("999.99"));
        product.setStockQuantity(10);
        product.setDescription("High-end gaming laptop");
        product.setExpiryDate(expiry);
        product.setImageUrl("http://example.com/image.jpg");
        product.setCategory(category);

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("SKU123", product.getSku());
        assertEquals(new BigDecimal("999.99"), product.getPrice());
        assertEquals(10, product.getStockQuantity());
        assertEquals("High-end gaming laptop", product.getDescription());
        assertEquals(expiry, product.getExpiryDate());
        assertEquals("http://example.com/image.jpg", product.getImageUrl());
        assertEquals(category, product.getCategory());
        assertNotNull(product.getCreatedAt());
    }

    @Test
    void testProductBuilder() {
        Category category = new Category();
        LocalDateTime expiry = LocalDateTime.of(2025, 12, 31, 23, 59);

        Product product = Product.builder()
                .id(2L)
                .name("Phone")
                .sku("SKU456")
                .price(new BigDecimal("499.99"))
                .stockQuantity(20)
                .description("Smartphone with AMOLED display")
                .expiryDate(expiry)
                .imageUrl("http://example.com/phone.jpg")
                .category(category)
                .build();

        assertEquals(2L, product.getId());
        assertEquals("Phone", product.getName());
        assertEquals("SKU456", product.getSku());
        assertEquals(new BigDecimal("499.99"), product.getPrice());
        assertEquals(20, product.getStockQuantity());
        assertEquals("Smartphone with AMOLED display", product.getDescription());
        assertEquals(expiry, product.getExpiryDate());
        assertEquals("http://example.com/phone.jpg", product.getImageUrl());
        assertEquals(category, product.getCategory());
        assertNotNull(product.getCreatedAt());
    }

    @Test
    void testToString() {
        Product product = new Product();
        product.setId(3L);
        product.setName("Tablet");
        product.setSku("SKU789");
        product.setPrice(new BigDecimal("299.99"));
        product.setStockQuantity(5);
        product.setDescription("Compact tablet");
        product.setExpiryDate(LocalDateTime.of(2026, 1, 1, 0, 0));
        product.setImageUrl("http://example.com/tablet.jpg");

        String output = product.toString();
        assertTrue(output.contains("id=3"));
        assertTrue(output.contains("name='Tablet'"));
        assertTrue(output.contains("sku='SKU789'"));
        assertTrue(output.contains("price=299.99"));
        assertTrue(output.contains("stockQuantity=5"));
        assertTrue(output.contains("description='Compact tablet'"));
        assertTrue(output.contains("imageUrl='http://example.com/tablet.jpg'"));
        assertTrue(output.contains("createdAt="));
    }
}