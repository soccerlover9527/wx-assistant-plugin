package implement.plugins;


import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.implement.plugins.DirtyWordMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author soccerlover9527@gmail.com
 */
@Slf4j
@Test
public class DirtyWorkHandlerTest {
    private DirtyWordMessageHandler dirtyWordMessageHandler;


    @BeforeClass
    public void init() {
        dirtyWordMessageHandler = new DirtyWordMessageHandler(null);
    }

    public void test() {
        var message = new Message();
        message.setContent("草泥马,吉尔，几把,你他吗的,word妈的");
        var response = dirtyWordMessageHandler.onMessage(message);
        Assert.assertNotNull(response);
        log.info("response: {}", response);
        System.out.println(response);
    }
}
