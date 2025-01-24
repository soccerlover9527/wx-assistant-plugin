package implement.plugins;


import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.implement.plugins.DirtyWordMessageHandler;
import com.soccerlover9527.wxassistant.plugin.implement.plugins.TokenizerWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

/**
 * @author soccerlover9527@gmail.com
 */
@Slf4j
public class DirtyWorkHandlerTest {
    private DirtyWordMessageHandler dirtyWordMessageHandler = new DirtyWordMessageHandler(new TokenizerWrapper(null), null);


    @Test
    public void test() {
        var message = new Message();
        message.setContent("草泥马吉尔几把你他妈的word妈的,操你妈的能不能好好说话");
        var response = dirtyWordMessageHandler.onMessage(message);
        log.info("response: {}", response);
    }

    @Test
    public void test1() throws IOException {
        Dictionary.getSingleton().addWords(Collections.singleton("昨天"));
        String message = "昨天才吃的火锅";
        StringReader stringReader = new StringReader(message);
        IKSegmenter ikSegmenter = new IKSegmenter(stringReader, true);
        Lexeme next = ikSegmenter.next();
        while (next != null) {
            System.out.println(next.getLexemeText());
            next = ikSegmenter.next();
        }
    }
}
