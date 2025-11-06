package com.hcltech.InventoryMgtSystem.services;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.dtos.SupplierDTO;
import com.hcltech.InventoryMgtSystem.exceptions.NotFoundException;
import com.hcltech.InventoryMgtSystem.models.Supplier;
import com.hcltech.InventoryMgtSystem.repositories.SupplierRepository;
import com.hcltech.InventoryMgtSystem.services.impl.SupplierServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {
    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private SupplierDTO supplierDTO;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplierDTO = new SupplierDTO();
        supplierDTO.setName("Test Supplier");
        supplierDTO.setContactInfo("1234567890");
        supplierDTO.setAddress("Test Address");

        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");
        supplier.setContactInfo("1234567890");
        supplier.setAddress("Test Address");
    }

    @Test
    void testAddSupplier() {
        when(modelMapper.map(supplierDTO, Supplier.class)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        Response response = supplierService.addSupplier(supplierDTO);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("Supplier Saved Successfully", response.getMessage())
        );

        verify(modelMapper).map(supplierDTO, Supplier.class);
        verify(supplierRepository).save(supplier);
    }

    @Test
    void testUpdateSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        supplierDTO.setName("Updated Name");

        Response response = supplierService.updateSupplier(1L, supplierDTO);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("Supplier Was Successfully Updated", response.getMessage()),
                () -> assertEquals("Updated Name", supplier.getName())
        );

        verify(supplierRepository).findById(1L);
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void testUpdateSupplier_NotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> supplierService.updateSupplier(1L, supplierDTO));

        verify(supplierRepository).findById(1L);
        verify(supplierRepository, never()).save(any());
    }

    @Test
    void testGetAllSupplier() {
        List<Supplier> suppliers = List.of(supplier);
        List<SupplierDTO> supplierDTOList = List.of(supplierDTO);

        when(supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(suppliers);
        Type listType = new TypeToken<List<SupplierDTO>>() {}.getType();
        when(modelMapper.map(suppliers, listType)).thenReturn(supplierDTOList);

        Response response = supplierService.getAllSupplier();

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("success", response.getMessage()),
                () -> assertEquals(1, response.getSuppliers().size())
        );

        verify(supplierRepository).findAll(Sort.by(Sort.Direction.DESC, "id"));
        verify(modelMapper).map(suppliers, listType);
    }

    @Test
    void testGetSupplierById() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(modelMapper.map(supplier, SupplierDTO.class)).thenReturn(supplierDTO);

        Response response = supplierService.getSupplierById(1L);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("success", response.getMessage()),
                () -> assertEquals("Test Supplier", response.getSupplier().getName())
        );

        verify(supplierRepository).findById(1L);
        verify(modelMapper).map(supplier, SupplierDTO.class);
    }

    @Test
    void testGetSupplierById_NotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> supplierService.getSupplierById(1L));

        verify(supplierRepository).findById(1L);
        verify(modelMapper, never()).map(any(), eq(SupplierDTO.class));
    }

    @Test
    void testDeleteSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        doNothing().when(supplierRepository).deleteById(1L);

        Response response = supplierService.deleteSupplier(1L);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("Supplier Was Successfully Deleted", response.getMessage())
        );

        verify(supplierRepository).findById(1L);
        verify(supplierRepository).deleteById(1L);
    }

    @Test
    void testDeleteSupplier_NotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> supplierService.deleteSupplier(1L));

        verify(supplierRepository).findById(1L);
        verify(supplierRepository, never()).deleteById(anyLong());
    }
}
