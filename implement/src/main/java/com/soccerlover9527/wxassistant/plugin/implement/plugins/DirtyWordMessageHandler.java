package com.soccerlover9527.wxassistant.plugin.implement.plugins;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.soccerlover9527.wxassistant.plugin.base.IWxCommandHandler;
import com.soccerlover9527.wxassistant.plugin.base.IWxMessageHandler;
import com.soccerlover9527.wxassistant.plugin.base.annotation.WxAssistantPlugin;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.CommandMessage;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.implement.repo.TermRepository;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author soccerlover9527@gmail.com
 */
@Component
@WxAssistantPlugin("dirty-word")
public class DirtyWordMessageHandler implements IWxMessageHandler, IWxCommandHandler, CommandLineRunner {
    private static final Set<String> DIRTY_PINYIN_SET = new HashSet<>();
    private static final HanyuPinyinOutputFormat HANYU_PINYIN_OUTPUT_FORMAT = new HanyuPinyinOutputFormat();
    private final TermRepository termRepository;

    public DirtyWordMessageHandler(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @Override
    public WxAssistantResponse onMessage(Message message) {
        var content = message.getContent();
        var terms = HanLP.segment(content).stream().map(term -> term.word).toList();
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
            return WxAssistantResponse.text(message.getGid(), "⚠黄牌警告：臧话卫士发现群友 " + message.getUsername() + " 说藏话[" +
                    StringUtils.join(badWords, ",") + "]，扣除积分" + points + "分！");
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

    @Override
    public WxAssistantResponse onMessage(CommandMessage message) {
        String command = message.getCommand();

        return null;
    }

    @Override
    public List<String> supportCommands() {
        return List.of("黑话", "脏话");
    }

    @Override
    public void run(String... args) throws Exception {
        HANYU_PINYIN_OUTPUT_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        HANYU_PINYIN_OUTPUT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        HANYU_PINYIN_OUTPUT_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);

        loadCustomTerms();

        loadDirtyPinyin();
    }


    private void loadDirtyPinyin() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("dict/dirty_pinyin.txt");
            InputStream inputStream = classPathResource.getInputStream();
            var customTermStr = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            if (StringUtils.isBlank(customTermStr)) {
                return;
            }
            DIRTY_PINYIN_SET.addAll(Stream.of(customTermStr.split("\r\n")).map(String::trim).collect(Collectors.toSet()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomTerms() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("dict/terms.txt");
            var inputStream = classPathResource.getInputStream();
            var customTermStr = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8).trim();
            if (StringUtils.isBlank(customTermStr)) {
                return;
            }
            String[] terms = customTermStr.split("\r\n");
            for (String term : terms) {
                CustomDictionary.add(term.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
