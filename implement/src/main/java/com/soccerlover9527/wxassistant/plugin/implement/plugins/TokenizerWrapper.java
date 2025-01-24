/* Copyright (c) 2025, TD SYNNEX Corporation. All rights reserved */
package com.soccerlover9527.wxassistant.plugin.implement.plugins;

import com.soccerlover9527.wxassistant.plugin.implement.repo.Term;
import com.soccerlover9527.wxassistant.plugin.implement.repo.TermRepository;
import com.soccerlover9527.wxassistant.plugin.implement.repo.TermTypeEnum;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author soccerlover9527@gmail.com
 * @since 2025-01-24
 */
@Component
public class TokenizerWrapper {
    private static final IKSegmenter IK_SEGMENTER = new IKSegmenter(null, true);
    private final TermRepository termRepository;

    public TokenizerWrapper(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @PostConstruct
    public void init() {
        //  load custom terms
        List<Term> terms = termRepository.findByTypeAndActive(TermTypeEnum.TERM.getType(), true);
        Dictionary.getSingleton().addWords(terms.stream().map(Term::getTerm).collect(Collectors.toSet()));
    }

    public List<String> tokenize(String text) {
        var result = new ArrayList<String>();
        //  分词器线程不安全,且每次 new 会重新加载资源,串行调用.
        synchronized (IK_SEGMENTER) {
            try {
                IK_SEGMENTER.reset(new StringReader(text));
                Lexeme next;
                next = IK_SEGMENTER.next();
                while (next != null) {
                    result.add(next.getLexemeText());
                    next = IK_SEGMENTER.next();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
