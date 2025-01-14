package implement.plugins;

import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.implement.plugins.MessageRepeaterHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author soccerlover9527@gmail.com
 */
@Test
public class MessageRepeaterHandlerTest {

    private static MessageRepeaterHandler handler;

    @BeforeClass
    public void before() {
        handler = new MessageRepeaterHandler();
    }

    public void testRepeatSuccess() {
        var message = new Message();
        message.setGid("g1");
        message.setContent("test1");

        var wxAssistantResponse = handler.onMessage(message);
        var wxAssistantResponse1 = handler.onMessage(message);
        var wxAssistantResponse2 = handler.onMessage(message);

        Assert.assertNull(wxAssistantResponse);
        Assert.assertNull(wxAssistantResponse1);
        Assert.assertNotNull(wxAssistantResponse2);
        Assert.assertEquals(wxAssistantResponse2.getBody(), message.getContent());
    }

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

        Assert.assertNull(wxAssistantResponse);
        Assert.assertNull(wxAssistantResponse1);
        Assert.assertNull(wxAssistantResponse2);
    }

    public void testRepeatAfterDelay() {
        handler.setRepeatSelfDelay(3000L);
        var message = new Message();
        message.setGid("g1");
        message.setContent("test3");

        var wxAssistantResponse = handler.onMessage(message);
        var wxAssistantResponse1 = handler.onMessage(message);
        var wxAssistantResponse2 = handler.onMessage(message);

        Assert.assertNull(wxAssistantResponse);
        Assert.assertNull(wxAssistantResponse1);
        Assert.assertEquals(wxAssistantResponse2.getBody(), message.getContent());
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        var wxAssistantResponse3 = handler.onMessage(message);
        Assert.assertNotNull(wxAssistantResponse3);
        Assert.assertEquals(wxAssistantResponse3.getBody(), message.getContent());
    }
}
