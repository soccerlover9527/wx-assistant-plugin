/* Copyright (c) 2025, TD SYNNEX Corporation. All rights reserved */
package com.soccerlover9527.wxassistant.plugin.implement.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p></p>
 *
 * @author Coulson.He@tdsynnex.com
 * @since 2025-01-15
 */
@Repository
public interface TermRepository extends CrudRepository<Term, Integer> {
    Term findByTerm(String term);

    List<Term> findByTypeAndActive(Integer type, Boolean active);
}
