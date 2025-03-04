/* Copyright (c) 2025, TD SYNNEX Corporation. All rights reserved */
package com.soccerlover9527.wxassistant.plugin.implement.repo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author soccerlover9527@gmail.com
 * @since 2025-01-15
 */
@Table(name = "term")
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "term", unique = true, nullable = false, length = 64)
    private String term;
    /**
     * 1. normal term 2. dirty words pinyin
     */
    @Column(name = "type", nullable = false)
    private Integer type;
    @Column(name = "active", nullable = false)
    private Boolean active;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "created_by", nullable = false, length = 64)
    private String createdBy;
}
