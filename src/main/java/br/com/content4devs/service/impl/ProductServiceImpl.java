package br.com.content4devs.service.impl;

import br.com.content4devs.domain.Product;
import br.com.content4devs.exception.ProductAlreadyExistsException;
import br.com.content4devs.exception.ProductNotFoundException;
import br.com.content4devs.resources.dto.ProductInputDTO;
import br.com.content4devs.resources.dto.ProductOutputDTO;
import br.com.content4devs.repository.ProductRepository;
import br.com.content4devs.service.IProductService;
import br.com.content4devs.util.ProductConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        checkDuplicity(inputDTO.getName());
        var product = ProductConverterUtil.productInputDtoToProduct(inputDTO);

        Product productCreate = this.productRepository.save(product);
        return ProductConverterUtil.produtctToProductOutputDTO(productCreate);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        var product = this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return ProductConverterUtil.produtctToProductOutputDTO(product);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ProductOutputDTO> findAll() {
        return null;
    }

    @Override
    public void checkDuplicity(String name) {
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> {
                    throw new ProductAlreadyExistsException(name);
                });
    }
}
