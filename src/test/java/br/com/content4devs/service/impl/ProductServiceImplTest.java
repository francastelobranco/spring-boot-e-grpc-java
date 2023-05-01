package br.com.content4devs.service.impl;

import br.com.content4devs.domain.Product;
import br.com.content4devs.exception.ProductAlreadyExistsException;
import br.com.content4devs.exception.ProductNotFoundException;
import br.com.content4devs.repository.ProductRepository;
import br.com.content4devs.resources.dto.ProductInputDTO;
import br.com.content4devs.resources.dto.ProductOutputDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    public void createProductSucessTest() {
        Product product = new Product(1L, "product name", 10.0, 10);
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductInputDTO inputDTO = new ProductInputDTO("product name", 10.0, 10);
        ProductOutputDTO outputDTO = productService.create(inputDTO);

        Assertions.assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    public void createProductExceptionTest() {
        Product product = new Product(1L, "product name", 10.0, 10);
        Mockito.when(productRepository.findByNameIgnoreCase(Mockito.any())).thenReturn(Optional.of(product));

        ProductInputDTO inputDTO = new ProductInputDTO("product name", 10.0, 10);

        Assertions.assertThatExceptionOfType(ProductAlreadyExistsException.class)
                        .isThrownBy(() -> productService.create(inputDTO));
    }

    @Test
    void findByIdSucessTest() {
        Long id = 1L;
        Product product = new Product(1L, "product name", 10.0, 10);

        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        ProductOutputDTO outputDTO = productService.findById(id);

        Assertions.assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    public void findByIdProductExceptionTest() {
        Long id = 1L;

        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.findById(id));
    }

    @Test
    void deleteSucessTest() {
        Long id = 1L;
        Product product = new Product(1L, "product name", 10.0, 10);

        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        Assertions.assertThatNoException().isThrownBy(() -> productService.findById(id));
    }

    @Test
    void deleteIdProductExceptionTest() {
        Long id = 1L;

        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.delete(id));
    }

    @Test
    void findAll() {
    }
}