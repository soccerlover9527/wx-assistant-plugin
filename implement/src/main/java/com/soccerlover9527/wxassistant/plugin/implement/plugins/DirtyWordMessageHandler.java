package com.soccerlover9527.wxassistant.plugin.implement.plugins;

import com.soccerlover9527.wxassistant.plugin.base.IWxCommandHandler;
import com.soccerlover9527.wxassistant.plugin.base.IWxMessageHandler;
import com.soccerlover9527.wxassistant.plugin.base.annotation.WxAssistantPlugin;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.CommandMessage;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.implement.repo.Term;
import com.soccerlover9527.wxassistant.plugin.implement.repo.TermRepository;
import com.soccerlover9527.wxassistant.plugin.implement.repo.TermTypeEnum;
import jakarta.annotation.PostConstruct;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.dic.Dictionary;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author soccerlover9527@gmail.com
 */
@Component
@WxAssistantPlugin("dirty-word")
public class DirtyWordMessageHandler implements IWxMessageHandler, IWxCommandHandler {
    private static final Set<String> DIRTY_PINYIN_SET = new HashSet<>();
    private static final HanyuPinyinOutputFormat HANYU_PINYIN_OUTPUT_FORMAT = new HanyuPinyinOutputFormat();
    private static final Logger log = LoggerFactory.getLogger(DirtyWordMessageHandler.class);
    private final TokenizerWrapper tokenizerWrapper;
    private final TermRepository termRepository;

    public DirtyWordMessageHandler(TokenizerWrapper tokenizerWrapper, TermRepository termRepository) {
        this.tokenizerWrapper = tokenizerWrapper;
        this.termRepository = termRepository;
    }

    @PostConstruct
    public void init() {
        HANYU_PINYIN_OUTPUT_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        HANYU_PINYIN_OUTPUT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        HANYU_PINYIN_OUTPUT_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);

        List<Term> dirtyWordsPinyin = termRepository.findByTypeAndActive(TermTypeEnum.DIRTY_WORD.getType(), true);
        dirtyWordsPinyin.forEach(term -> DIRTY_PINYIN_SET.add(term.getTerm()));
    }

    @Override
    public WxAssistantResponse onMessage(Message message) {
        var content = message.getContent();
        if (content.startsWith(CUSTOM_COMMAND) || content.startsWith(DIRTY_COMMAND)) {
            return null;
        }
        List<String> terms = tokenizerWrapper.tokenize(content);
        var badWords = new ArrayList<>();
        for (String term : terms) {
            var termPinyin = textToPinyin(term);
            if (DIRTY_PINYIN_SET.contains(termPinyin)) {
                badWords.add(term);
            }
        }
        if (!badWords.isEmpty()) {
            var random = new Random();
            var points = random.nextInt(20) + 1;
            return WxAssistantResponse.text(message.getGid(), "⚠黄牌警告：脏话卫士发现群友 " + message.getUsername() + " 说脏话[" + StringUtils.join(badWords, ",") + "]，扣除积分" + points + "分！");
        } else {
            return null;
        }
    }

    public static String textToPinyin(String text) {
        var charArray = text.trim().toCharArray();
        var result = new StringBuilder();
        for (char c : charArray) {
            if (c > 0x4e00 && c < 0x9fa5) {
                try {
                    var pinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(c, HANYU_PINYIN_OUTPUT_FORMAT);
                    result.append(pinyinStringArray[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    throw new RuntimeException(e);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static final String CUSTOM_COMMAND = "黑话";
    private static final String DIRTY_COMMAND = "脏话";

    @Override
    public WxAssistantResponse onMessage(CommandMessage message) {
        var command = message.getCommand();
        if (!"soccerlover.".equals(message.getUsername())) {
            return null;
        }
        var parameter = message.getParameter();
        if (StringUtils.isBlank(parameter)) {
            return null;
        }
        var params = parameter.split(" ");
        if (params.length != 2 || (!"-a".equals(params[0]) && !"-d".equals(params[0]))) {
            return WxAssistantResponse.text(message.getGid(), "[" + command + "]:参数格式错误，正确格式为：" + command + " -[a|d] [term]");
        }
        var term = params[1];
        if (term.length() > 32) {
            return WxAssistantResponse.text(message.getGid(), "[" + command + "]:[" + parameter + "]长度不能超过32个字符");
        }
        if ("-a".equals(params[0])) {
            return createTerm(message, parameter, command, term);
        } else {
            return deleteTerm(message, term, command);
        }
    }

    private WxAssistantResponse deleteTerm(CommandMessage message, String term, String command) {
        termRepository.deleteByTerm(term);
        Dictionary.getSingleton().disableWords(Collections.singleton(term));
        return WxAssistantResponse.text(message.getGid(), "删除[" + command + "]:[" + term + "]成功");
    }

    private WxAssistantResponse createTerm(CommandMessage message, String parameter, String command, String term) {
        if (termRepository.findByTerm(term) != null) {
            return WxAssistantResponse.text(message.getGid(), "[" + command + "]:[" + parameter + "]已存在");
        }
        Term termPo = Term.builder().term(term).active(true).createdAt(LocalDateTime.now()).createdBy(message.getUsername()).build();
        if (CUSTOM_COMMAND.equals(command)) {
            log.info("{} add customer term:{}", message.getUsername(), parameter);
            termPo.setType(TermTypeEnum.TERM.getType());
        } else if (DIRTY_COMMAND.equals(command)) {
            log.info("{} add dirty term:{}", message.getUsername(), parameter);
            termPo.setType(TermTypeEnum.DIRTY_WORD.getType());
        }
        termRepository.save(termPo);
        Dictionary.getSingleton().addWords(Set.of(termPo.getTerm()));
        return WxAssistantResponse.text(message.getGid(), "添加[" + command + "]:[" + term + "]成功");
    }

    @Override
    public List<String> supportCommands() {
        return List.of(CUSTOM_COMMAND, DIRTY_COMMAND);
    }
}
