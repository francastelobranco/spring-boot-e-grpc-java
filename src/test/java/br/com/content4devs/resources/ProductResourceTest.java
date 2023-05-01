package br.com.content4devs.resources;

import br.com.content4devs.*;
import br.com.content4devs.domain.Product;
import br.com.content4devs.exception.ProductNotFoundException;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
class ProductResourceTest {

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @Autowired
    private Flyway flyway;
    @BeforeEach
    public void setUp(){
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void createProductSucessTest() {
        ProductRequest productRequest = ProductRequest.newBuilder()
                .setName("product name")
                .setPrice(10.00)
                .setQuantityInStock(100)
                .build();

        ProductResponse productResponse = serviceBlockingStub.create(productRequest);

        Assertions.assertThat(productRequest)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantity_in_stock")
                .isEqualTo(productResponse);
    }

    @Test
    void createProductAlreadyExistisExceptionTest() {
        ProductRequest productRequest = ProductRequest.newBuilder()
                .setName("Product A")
                .setPrice(10.00)
                .setQuantityInStock(100)
                .build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.create(productRequest))
                .withMessage("ALREADY_EXISTS: Produto Product A já cadastrado no sistema.");
    }

    @Test
    void findByIdProductSucessTest() {
        RequestById request = RequestById.newBuilder()
                .setId(1L)
                .build();

        ProductResponse productResponse = serviceBlockingStub.findById(request);
        Assertions.assertThat(productResponse.getId()).isEqualTo(request.getId());
    }

    @Test
    void findByIdProductNotFoundExceptionTest() {
        RequestById request = RequestById.newBuilder()
                .setId(4L)
                .build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.findById(request))
                .withMessage("NOT_FOUND: Produto com ID 4 não encontrado.");
    }

    @Test
    void deleteProductIdProductSucessTest() {
        RequestById request = RequestById.newBuilder()
                .setId(1L)
                .build();

        Assertions.assertThatNoException().isThrownBy(() -> serviceBlockingStub.delete(request));
    }

    @Test
    void deleteProductNotFoundExceptionTest() {
        RequestById request = RequestById.newBuilder()
                .setId(4L)
                .build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.delete(request))
                .withMessage("NOT_FOUND: Produto com ID 4 não encontrado.");
    }

    @Test
    void findAllProductSucessTest() {
        EmptyRequest request = EmptyRequest.newBuilder().build();

        ProductResponseList responseList = serviceBlockingStub.findAll(request);

        Assertions.assertThat(responseList).isInstanceOf(ProductResponseList.class);

        Assertions.assertThat(responseList.getProductsCount()).isEqualTo(2);

        Assertions.assertThat(responseList.getProductsList())
                .extracting("id", "name", "price", "quantityInStock")
                .contains(
                        Tuple.tuple(1L,"Product A", 10.99, 10),
                        Tuple.tuple(2L, "Product B", 10.99, 10)
                );
    }
}