package com.hcltech.InventoryMgtSystem.models;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    @Test
    void testSupplierFields() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("ABC Supplies");
        supplier.setContactInfo("abc@example.com");
        supplier.setAddress("123 Market Street");

        assertEquals(1L, supplier.getId());
        assertEquals("ABC Supplies", supplier.getName());
        assertEquals("abc@example.com", supplier.getContactInfo());
        assertEquals("123 Market Street", supplier.getAddress());
    }

    @Test
    void testSupplierBuilder() {
        Supplier supplier = Supplier.builder()
                .id(2L)
                .name("XYZ Traders")
                .contactInfo("xyz@example.com")
                .address("456 Commerce Avenue")
                .build();

        assertEquals(2L, supplier.getId());
        assertEquals("XYZ Traders", supplier.getName());
        assertEquals("xyz@example.com", supplier.getContactInfo());
        assertEquals("456 Commerce Avenue", supplier.getAddress());
    }
}
