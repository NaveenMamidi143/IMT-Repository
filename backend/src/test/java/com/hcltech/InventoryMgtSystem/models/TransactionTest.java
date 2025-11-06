package com.hcltech.InventoryMgtSystem.models;

import com.hcltech.InventoryMgtSystem.enums.TransactionStatus;
import com.hcltech.InventoryMgtSystem.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testDefaultConstructorAndSetters() {
        Transaction transaction = new Transaction();
        assertNotNull(transaction);

        transaction.setId(10L);
        transaction.setTotalProducts(1);
        transaction.setTotalPrice(BigDecimal.ZERO);
        transaction.setTransactionType(TransactionType.RETURN_TO_SUPPLIER);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setDescription(null);
        transaction.setNote(null);
        transaction.setUpdateAt(null);
        transaction.setProduct(new Product());
        transaction.setUser(new User());
        transaction.setSupplier(new Supplier());

        assertEquals(10L, transaction.getId());
        assertEquals(1, transaction.getTotalProducts());
        assertEquals(BigDecimal.ZERO, transaction.getTotalPrice());
        assertEquals(TransactionType.RETURN_TO_SUPPLIER, transaction.getTransactionType());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertNull(transaction.getDescription());
        assertNull(transaction.getNote());
        assertNull(transaction.getUpdateAt());
        assertNotNull(transaction.getProduct());
        assertNotNull(transaction.getUser());
        assertNotNull(transaction.getSupplier());
        assertNotNull(transaction.getCreatedAt());
    }

    @Test
    void testTransactionBuilder() {
        Product product = new Product();
        User user = new User();
        Supplier supplier = new Supplier();
        LocalDateTime updateTime = LocalDateTime.of(2025, 11, 4, 10, 0);

        Transaction transaction = Transaction.builder()
                .id(2L)
                .totalProducts(3)
                .totalPrice(new BigDecimal("900.00"))
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.PROCESSING)
                .description("Retail sale")
                .note("Awaiting shipment")
                .updateAt(updateTime)
                .product(product)
                .user(user)
                .supplier(supplier)
                .build();

        assertEquals(2L, transaction.getId());
        assertEquals(3, transaction.getTotalProducts());
        assertEquals(new BigDecimal("900.00"), transaction.getTotalPrice());
        assertEquals(TransactionType.SALE, transaction.getTransactionType());
        assertEquals(TransactionStatus.PROCESSING, transaction.getStatus());
        assertEquals("Retail sale", transaction.getDescription());
        assertEquals("Awaiting shipment", transaction.getNote());
        assertEquals(updateTime, transaction.getUpdateAt());
        assertEquals(product, transaction.getProduct());
        assertEquals(user, transaction.getUser());
        assertEquals(supplier, transaction.getSupplier());
        assertNotNull(transaction.getCreatedAt());
    }

    @Test
    void testToString() {
        Transaction transaction = new Transaction();
        transaction.setId(3L);
        transaction.setTotalProducts(2);
        transaction.setTotalPrice(new BigDecimal("500.00"));
        transaction.setTransactionType(TransactionType.RETURN_TO_SUPPLIER);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setDescription("Return due to defect");
        transaction.setNote("Pending approval");
        transaction.setUpdateAt(LocalDateTime.of(2025, 11, 4, 11, 0));

        String output = transaction.toString();
        assertTrue(output.contains("id=3"));
        assertTrue(output.contains("totalProducts=2"));
        assertTrue(output.contains("totalPrice=500.00"));
        assertTrue(output.contains("transactionType=RETURN_TO_SUPPLIER"));
        assertTrue(output.contains("status=PENDING"));
        assertTrue(output.contains("description='Return due to defect'"));
        assertTrue(output.contains("note='Pending approval'"));
        assertTrue(output.contains("createdAt="));
        assertTrue(output.contains("updateAt=2025-11-04T11:00"));
    }

    @Test
    void testEnumCoverage() {
        for (TransactionType type : TransactionType.values()) {
            assertNotNull(type.name());
        }

        for (TransactionStatus status : TransactionStatus.values()) {
            assertNotNull(status.name());
        }
    }
}