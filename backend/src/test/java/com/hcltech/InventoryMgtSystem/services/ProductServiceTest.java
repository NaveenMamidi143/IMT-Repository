package com.hcltech.InventoryMgtSystem.services;
import com.hcltech.InventoryMgtSystem.dtos.ProductDTO;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.exceptions.NotFoundException;
import com.hcltech.InventoryMgtSystem.models.Category;
import com.hcltech.InventoryMgtSystem.models.Product;
import com.hcltech.InventoryMgtSystem.repositories.CategoryRepository;
import com.hcltech.InventoryMgtSystem.repositories.ProductRepository;
import com.hcltech.InventoryMgtSystem.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private MultipartFile imageFile;

    @InjectMocks private ProductServiceImpl productService;

    private ProductDTO productDTO;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setName("Laptop");
        productDTO.setSku("LAP123");
        productDTO.setPrice(BigDecimal.valueOf(999.99));
        productDTO.setStockQuantity(10);
        productDTO.setDescription("High-end laptop");
        productDTO.setCategoryId(1L);

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .sku("LAP123")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .description("High-end laptop")
                .category(category)
                .build();
    }

    @Test
    void testSaveProduct_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(imageFile.isEmpty()).thenReturn(true); // simulate no image
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Response response = productService.saveProduct(productDTO, imageFile);

        assertEquals(200, response.getStatus());
        assertEquals("Product successfully saved", response.getMessage());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testSaveProduct_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.saveProduct(productDTO, imageFile));
    }

    @Test
    void testUpdateProduct_Success() {
        // Mock existing product
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Mock category lookup
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Simulate no image
        when(imageFile.isEmpty()).thenReturn(true);

        // Mock save
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Call service
        Response response = productService.updateProduct(productDTO, imageFile);

        // Assertions
        assertEquals(200, response.getStatus());
        assertEquals("Product Updated successfully", response.getMessage());

        // Verify interactions
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(productDTO, imageFile));
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(product);
        List<ProductDTO> productDTOList = List.of(productDTO);

        when(productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(products);
        Type listType = new TypeToken<List<ProductDTO>>() {}.getType();
        when(modelMapper.map(products, listType)).thenReturn(productDTOList);

        Response response = productService.getAllProducts();

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(1, response.getProducts().size());
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        Response response = productService.getProductById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals("Laptop", response.getProduct().getName());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        Response response = productService.deleteProduct(1L);

        assertEquals(200, response.getStatus());
        assertEquals("Product Deleted successfully", response.getMessage());
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void testSearchProduct_Success() {
        List<Product> products = List.of(product);
        List<ProductDTO> productDTOList = List.of(productDTO);

        when(productRepository.findByNameContainingOrDescriptionContaining("Laptop", "Laptop")).thenReturn(products);
        Type listType = new TypeToken<List<ProductDTO>>() {}.getType();
        when(modelMapper.map(products, listType)).thenReturn(productDTOList);

        Response response = productService.searchProduct("Laptop");

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(1, response.getProducts().size());
    }

    @Test
    void testSearchProduct_NotFound() {
        when(productRepository.findByNameContainingOrDescriptionContaining("Laptop", "Laptop")).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> productService.searchProduct("Laptop"));
    }
}