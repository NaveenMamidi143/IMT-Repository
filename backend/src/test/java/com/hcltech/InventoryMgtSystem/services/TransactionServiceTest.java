package com.hcltech.InventoryMgtSystem.services;
import com.hcltech.InventoryMgtSystem.dtos.*;
import com.hcltech.InventoryMgtSystem.enums.TransactionStatus;
import com.hcltech.InventoryMgtSystem.enums.TransactionType;
import com.hcltech.InventoryMgtSystem.exceptions.NameValueRequiredException;
import com.hcltech.InventoryMgtSystem.exceptions.NotFoundException;
import com.hcltech.InventoryMgtSystem.models.*;
import com.hcltech.InventoryMgtSystem.repositories.ProductRepository;
import com.hcltech.InventoryMgtSystem.repositories.SupplierRepository;
import com.hcltech.InventoryMgtSystem.repositories.TransactionRepository;
import com.hcltech.InventoryMgtSystem.services.UserService;
import com.hcltech.InventoryMgtSystem.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private TransactionServiceImpl transactionService; // ✅ Correct usage
    private Product product;
    private Supplier supplier;
    private User user;
    private TransactionRequest request;
    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(100));
        product.setStockQuantity(10);

        supplier = new Supplier();
        supplier.setId(1L);

        user = new User();
        user.setId(1L);

        request = new TransactionRequest();
        request.setProductId(1L);
        request.setSupplierId(1L);
        request.setQuantity(5);
        request.setDescription("Test Desc");
        request.setNote("Test Note");
    }

    @Test
    void testPurchase() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(userService.getCurrentLoggedInUser()).thenReturn(user);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Response response = transactionService.purchase(request);

        assertEquals(200, response.getStatus());
        assertEquals("Purchase Made successfully", response.getMessage());
        assertEquals(15, product.getStockQuantity());
    }

    @Test
    void testPurchase_MissingSupplierId() {
        request.setSupplierId(null);
        assertThrows(NameValueRequiredException.class, () -> transactionService.purchase(request));
    }

    @Test
    void testSell() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userService.getCurrentLoggedInUser()).thenReturn(user);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Response response = transactionService.sell(request);

        assertEquals(200, response.getStatus());
        assertEquals("Product Sale successfully made", response.getMessage());
        assertEquals(5, product.getStockQuantity());
    }

    @Test
    void testReturnToSupplier() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(userService.getCurrentLoggedInUser()).thenReturn(user);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Response response = transactionService.returnToSupplier(request);

        assertEquals(200, response.getStatus());
        assertEquals("Product Returned in progress", response.getMessage());
        assertEquals(5, product.getStockQuantity());
    }

    @Test
    void testGetAllTransactions() {
        Transaction transaction = new Transaction();
        Page<Transaction> page = new PageImpl<>(List.of(transaction));
        TransactionDTO dto = new TransactionDTO();

        // Create the Type object outside the matcher
        Type listType = new TypeToken<List<TransactionDTO>>() {}.getType();

        when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Use eq(listType) safely
        when(modelMapper.map(eq(List.of(transaction)), eq(listType)))
                .thenReturn(List.of(dto));

        Response response = transactionService.getAllTransactions(0, 10, "filter");

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(1, response.getTransactions().size());
    }

    @Test
    void testGetAllTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setUser(user);

        TransactionDTO dto = new TransactionDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        dto.setUser(userDTO);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(modelMapper.map(transaction, TransactionDTO.class)).thenReturn(dto);

        Response response = transactionService.getAllTransactionById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
    }
    @Test
    void testGetAllTransactionByMonthAndYear() {
        Transaction transaction = new Transaction();
        TransactionDTO dto = new TransactionDTO();

        List<Transaction> transactions = List.of(transaction);
        List<TransactionDTO> dtos = List.of(dto);

        Type listType = new TypeToken<List<TransactionDTO>>() {}.getType(); // ✅ Create Type manually

        when(transactionRepository.findAll(any(Specification.class))).thenReturn(transactions);
        when(modelMapper.map(eq(transactions), eq(listType))).thenReturn(dtos); // ✅ Use eq(listType)

        Response response = transactionService.getAllTransactionByMonthAndYear(10, 2025);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(1, response.getTransactions().size());
    }

    @Test
    void testUpdateTransactionStatus() {
        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.PROCESSING);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Response response = transactionService.updateTransactionStatus(1L, TransactionStatus.COMPLETED);
    }
}