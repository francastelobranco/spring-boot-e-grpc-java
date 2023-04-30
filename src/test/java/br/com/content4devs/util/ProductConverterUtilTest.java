package br.com.content4devs.util;

import br.com.content4devs.domain.Product;
import br.com.content4devs.resources.dto.ProductInputDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductConverterUtilTest {

    @Test
    void produtctToProductOutputDtoTest() {
        var product = new Product(1L, "product name", 10.0, 10);
        var productOutputDto = ProductConverterUtil
                .produtctToProductOutputDTO(product);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productOutputDto);
    }

    @Test
    void productInputDtoToProductTest() {
        var productInput = new ProductInputDTO( "product name", 10.0, 10);
        var product = ProductConverterUtil
                .productInputDtoToProduct(productInput);

        Assertions.assertThat(productInput)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }
}