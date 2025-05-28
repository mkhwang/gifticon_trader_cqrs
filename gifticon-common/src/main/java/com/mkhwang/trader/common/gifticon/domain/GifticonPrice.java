package com.mkhwang.trader.common.gifticon.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "gifticon_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GifticonPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gifticon_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_gifticon_price_gifticon"))
  private Gifticon gifticon;

  @Column(name = "base_price", nullable = false, precision = 19, scale = 2)
  private BigDecimal basePrice;

  @Column(name = "sale_price", precision = 19, scale = 2)
  private BigDecimal salePrice;

  @Column(length = 3, nullable = false)
  @Builder.Default
  private String currency = "KRW";

  // Helper method to calculate discount percentage
  @Transient
  public Integer getDiscountPercentage() {
    if (basePrice == null || salePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
      return 0;
    }

    if (salePrice.compareTo(basePrice) >= 0) {
      return 0;
    }

    BigDecimal discount = basePrice.subtract(salePrice);
    BigDecimal percentage = discount.multiply(new BigDecimal("100")).divide(basePrice, 0, RoundingMode.HALF_UP);

    return percentage.intValue();
  }
}
