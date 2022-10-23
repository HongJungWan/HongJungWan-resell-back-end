package com.resell.resell.domain.trade;

import com.resell.resell.domain.BaseTimeEntity;
import com.resell.resell.domain.addressBook.Address;
import com.resell.resell.domain.product.Product;
import com.resell.resell.domain.users.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trade extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUBLISHER_ID")
    private User publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER_ID")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    private Long price;

    private double productSize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETURN_ID")
    private Address returnAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHIPPING_ID")
    private Address shippingAddress;

    private String receivingTrackingNumber;

    private String forwardingTrackingNumber;

    private String returnTrackingNumber;

    private String cancelReason;
    
    @Builder
    public Trade(Long id, User publisher, User seller, User buyer,
                 Product product, TradeStatus status, Long price, double productSize,
                 Address returnAddress, Address shippingAddress) {
        this.id = id;
        this.publisher = publisher;
        this.seller = seller;
        this.buyer = buyer;
        this.product = product;
        this.status = status;
        this.price = price;
        this.productSize = productSize;
        this.returnAddress = returnAddress;
        this.shippingAddress = shippingAddress;
    }

}