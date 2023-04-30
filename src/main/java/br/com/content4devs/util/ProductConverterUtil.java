package br.com.content4devs.util;

import br.com.content4devs.domain.Product;
import br.com.content4devs.resources.dto.ProductInputDTO;
import br.com.content4devs.resources.dto.ProductOutputDTO;

public class ProductConverterUtil {

    public static ProductOutputDTO produtctToProductOutputDTO(Product product){
        return new ProductOutputDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }

    public static Product productInputDtoToProduct(ProductInputDTO product){
        return new Product(
                null,
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }
}
