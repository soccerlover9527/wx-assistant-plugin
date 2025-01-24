package implement.plugins;

import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.implement.plugins.MessageRepeaterHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author soccerlover9527@gmail.com
 */

public class MessageRepeaterHandlerTest {

    private static MessageRepeaterHandler handler;

    @BeforeAll
    public static void before() {
        handler = new MessageRepeaterHandler();
    }

    @Test
    public void testRepeatSuccess() {
        var message = new Message();
        message.setGid("g1");
        message.setContent("test1");

        var wxAssistantResponse = handler.onMessage(message);
        var wxAssistantResponse1 = handler.onMessage(message);
        var wxAssistantResponse2 = handler.onMessage(message);

        Assertions.assertNull(wxAssistantResponse);
        Assertions.assertNull(wxAssistantResponse1);
        Assertions.assertNotNull(wxAssistantResponse2);
        Assertions.assertEquals(wxAssistantResponse2.getBody(), message.getContent());
    }

    @Test
    public void testRepeatIsolateGroup() {
        var message = new Message();
        message.setGid("g1");
        message.setContent("test2");

        var message1 = new Message();
        message1.setGid("g2");
        message1.setContent("test2");
        var wxAssistantResponse = handler.onMessage(message);
        var wxAssistantResponse1 = handler.onMessage(message);
        var wxAssistantResponse2 = handler.onMessage(message1);

        Assertions.assertNull(wxAssistantResponse);
        Assertions.assertNull(wxAssistantResponse1);
        Assertions.assertNull(wxAssistantResponse2);
    }

    @Test
    public void testRepeatAfterDelay() {
        handler.setRepeatSelfDelay(3000L);
        var message = new Message();
        message.setGid("g1");
        message.setContent("test3");

        var wxAssistantResponse = handler.onMessage(message);
        var wxAssistantResponse1 = handler.onMessage(message);
        var wxAssistantResponse2 = handler.onMessage(message);

        Assertions.assertNull(wxAssistantResponse);
        Assertions.assertNull(wxAssistantResponse1);
        Assertions.assertEquals(wxAssistantResponse2.getBody(), message.getContent());
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        var wxAssistantResponse3 = handler.onMessage(message);
        Assertions.assertNotNull(wxAssistantResponse3);
        Assertions.assertEquals(wxAssistantResponse3.getBody(), message.getContent());
    }
}
