package com.mkhwang.gifticon.service.entity;

import com.mkhwang.gifticon.config.audit.BaseCreateAudit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "search_keywords")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchKeyword extends BaseCreateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String keyword;

}
