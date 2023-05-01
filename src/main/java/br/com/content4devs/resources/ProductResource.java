package br.com.content4devs.resources;

import br.com.content4devs.*;
import br.com.content4devs.resources.dto.ProductInputDTO;
import br.com.content4devs.resources.dto.ProductOutputDTO;
import br.com.content4devs.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
        ProductOutputDTO outputDTO = productService.findById(request.getId());

        ProductResponse response = ProductResponse.newBuilder()
                .setId(outputDTO.getId())
                .setName(outputDTO.getName())
                .setPrice(outputDTO.getPrice())
                .setQuantityInStock(outputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RequestById request, StreamObserver<EmptyResponse> responseObserver) {
        productService.delete(request.getId());
        responseObserver.onNext(EmptyResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
        List<ProductOutputDTO> outputDtosList = productService.findAll();

        List<ProductResponse> productResponseList = outputDtosList.stream()
                .map(outputDTO -> ProductResponse.newBuilder()
                        .setId(outputDTO.getId())
                        .setName(outputDTO.getName())
                        .setPrice(outputDTO.getPrice())
                        .setQuantityInStock(outputDTO.getQuantityInStock())
                        .build())
                .collect(Collectors.toList());

        ProductResponseList response = ProductResponseList.newBuilder()
                .addAllProducts(productResponseList)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
