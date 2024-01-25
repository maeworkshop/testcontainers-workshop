package com.maemresen.tc.spring.cloud.stream.kafka.service;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.ShoppingCartItemSaveDto;
import com.maemresen.tc.spring.cloud.stream.kafka.dto.ShoppingCartSaveDto;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.Product;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.ShoppingCart;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.ShoppingCartItem;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.constants.OrderStatus;
import com.maemresen.tc.spring.cloud.stream.kafka.message.dto.OrderProcessMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.message.dto.StockUpdatedMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.message.producer.CustomerOrderProcessMessageProducer;
import com.maemresen.tc.spring.cloud.stream.kafka.repository.ProductRepository;
import com.maemresen.tc.spring.cloud.stream.kafka.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderManagementService {

    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    private final CustomerOrderProcessMessageProducer customerOrderProcessMessageProducer;

    public void updateProductStock(final StockUpdatedMessageDto stockUpdatedMessageDto) {
        final Product product = productRepository.findByExternalId(stockUpdatedMessageDto.id())
                .orElseGet(Product::new);
        product.setExternalId(stockUpdatedMessageDto.id());
        product.setStock(stockUpdatedMessageDto.newStock());
        product.setStockUpdated(stockUpdatedMessageDto.updated());
        productRepository.save(product);
    }

    public ShoppingCart createShoppingCart(final ShoppingCartSaveDto shoppingCartSaveDto) {
        final ShoppingCart shoppingCart = ShoppingCart.builder()
                .status(OrderStatus.PENDING)
                .username(shoppingCartSaveDto.getUsername())
                .build();
        final List<ShoppingCartItem> shoppingCartItems = shoppingCartSaveDto.getShoppingCartItems()
                .stream()
                .map((shoppingCartItemSaveDto -> getShoppingCartItem(shoppingCart, shoppingCartItemSaveDto)))
                .toList();
        shoppingCart.setShoppingCartItems(shoppingCartItems);
        return saveShoppingCart(shoppingCart);
    }

    public ShoppingCart cancelShoppingCart(final long shoppingCartId) {
        return updateShoppingCartStatus(shoppingCartId, OrderStatus.CANCELLED);
    }

    public ShoppingCart completeShoppingCart(final long shoppingCartId) {
        return updateShoppingCartStatus(shoppingCartId, OrderStatus.COMPLETED);
    }

    private ShoppingCart updateShoppingCartStatus(final long shoppingCartId, final OrderStatus orderStatus) {
        final ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).orElseThrow();
        shoppingCart.setStatus(orderStatus);
        return saveShoppingCart(shoppingCart);
    }

    public ShoppingCart addShoppingCartItem(final long shoppingCartId, final ShoppingCartItemSaveDto shoppingCartItemSaveDto) {
        final ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).orElseThrow();

        List<ShoppingCartItem> shoppingCartItems = shoppingCart.getShoppingCartItems();
        if (shoppingCartItems == null) {
            shoppingCartItems = new ArrayList<>();
            shoppingCart.setShoppingCartItems(shoppingCartItems);
        }
        shoppingCartItems.add(getShoppingCartItem(shoppingCart, shoppingCartItemSaveDto));
        return saveShoppingCart(shoppingCart);
    }

    public ShoppingCart removeShoppingCartItem(final long shoppingCartId, final long shoppingCartItemId) {
        final ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).orElseThrow();
        Optional.ofNullable(shoppingCart.getShoppingCartItems())
                .ifPresent(x -> x.removeIf(shoppingCartItem -> shoppingCartItem.getId().equals(shoppingCartItemId)));
        return saveShoppingCart(shoppingCart);
    }

    private ShoppingCart saveShoppingCart(final ShoppingCart shoppingCart) {
        final ShoppingCart saved = shoppingCartRepository.save(shoppingCart);
        customerOrderProcessMessageProducer.publish(OrderProcessMessageDto.builder()
                .id(shoppingCart.getId())
                .status(shoppingCart.getStatus())
                .timestamp(shoppingCart.getUpdated())
                .username(shoppingCart.getUsername())
                .build());
        return saved;
    }

    private ShoppingCartItem getShoppingCartItem(final ShoppingCart shoppingCart, final ShoppingCartItemSaveDto shoppingCartItemSaveDto) {
        final Product product = productRepository.findById(shoppingCartItemSaveDto.getProductId()).orElseThrow();
        final Long productCount = shoppingCartItemSaveDto.getProductCount();
        if (product.getStock() <= productCount) {
            throw new IllegalArgumentException("Not enough stock exists for the given product.");
        }
        return ShoppingCartItem.builder()
                .productCount(productCount)
                .shoppingCart(shoppingCart)
                .product(product)
                .build();
    }
}
