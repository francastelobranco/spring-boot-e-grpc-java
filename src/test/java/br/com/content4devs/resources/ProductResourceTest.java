package br.com.content4devs.resources;

import br.com.content4devs.ProductRequest;
import br.com.content4devs.ProductResponse;
import br.com.content4devs.ProductServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
class ProductResourceTest {

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

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
}