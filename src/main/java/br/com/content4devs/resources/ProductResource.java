package br.com.content4devs.resources;

import br.com.content4devs.ProductRequest;
import br.com.content4devs.ProductResponse;
import br.com.content4devs.ProductServiceGrpc;
import br.com.content4devs.RequestById;
import br.com.content4devs.resources.dto.ProductInputDTO;
import br.com.content4devs.resources.dto.ProductOutputDTO;
import br.com.content4devs.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

    private IProductService productService;

    public ProductResource(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        ProductInputDTO inputDto = new ProductInputDTO(
                request.getName(),
                request.getPrice(),
                request.getQuantityInStock()
        );

        ProductOutputDTO outputDTO = this.productService.create(inputDto);

        ProductResponse response = ProductResponse.newBuilder()
                .setId(outputDTO.getId())
                .setName(outputDTO.getName())
                .setPrice(outputDTO.getPrice())
                .setQuantityInStock(outputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

//    @Override
//    public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
//        ProductOutputDTO inputDto = new ProductOutputDTO(
//                request.getId()
//        );
//    }
}
