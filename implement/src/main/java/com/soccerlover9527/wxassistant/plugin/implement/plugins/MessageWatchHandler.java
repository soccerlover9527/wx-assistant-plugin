package com.soccerlover9527.wxassistant.plugin.implement.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccerlover9527.wxassistant.plugin.base.IWxMessageHandler;
import com.soccerlover9527.wxassistant.plugin.base.annotation.WxAssistantPlugin;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author like
 */
@Service
@WxAssistantPlugin("reply-and-bad-words")
public class MessageWatchHandler implements IWxMessageHandler {
    private final Logger log = LoggerFactory.getLogger(MessageWatchHandler.class);

    private final List<String> messageList = new CopyOnWriteArrayList<>();

    private final Map<String, Long> saidInfoMap = new ConcurrentHashMap<>();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper;

    public MessageWatchHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public WxAssistantResponse onMessage(Message message) {
        //判断message空值
        if (ObjectUtils.isEmpty(message)) {
            return null;
        }
        //判断消息类型是否为视频消息
        if (message.getMedia() != null) {
            log.info("group:{}, user:{}, say:{}, media:{}", message.getGroupName(), message.getUsername(), message.getContent(), message.getMedia().getSrc());
        }
        //不为视频消息处理文字消息
        else {
            String content = message.getContent();
            // 修改成显式同步
            synchronized (messageList) {
                //遍历当前存了的消息信息，判断是否已经发送过
                for (String messageInfo : messageList) {
                    //如果存在复读一次,判断小助手是否复读过一次，如果超过120秒则再次发送
                    if (content.equals(messageInfo)) {
                        Long currentTime = saidInfoMap.get(content);
                        long currentTimeMillis = System.currentTimeMillis();
                        if (null != currentTime) {
                            if ((currentTimeMillis - currentTime) / 1000 > 120) {
                                saidInfoMap.put(content, currentTimeMillis);
                                return WxAssistantResponse.text(message.getGid(), content);
                            }
                            //小助手没复读过就直接复读
                        } else {
                            saidInfoMap.put(content, currentTimeMillis);
                            return WxAssistantResponse.text(message.getGid(), content);
                        }
                        break;
                    }
                }
                if (messageList.size() > 20) {
                    messageList.remove(0);
                }
                messageList.add(content);
            }

            log.debug("group:{}, user:{}, say:{}", message.getGroupName(), message.getUsername(), message.getContent());
        }

        //判断是否说藏话
        String content = message.getContent();
        if (convertToPinyin(content)) {
            // 创建Random实例
            Random random = new Random();
            // 生成1到20之间的随机数
            int randomNumber = random.nextInt(20) + 1;
            try {
                Map<String, Object> dataInfo = new HashMap<>();
                dataInfo.put("gid", message.getGid());
                dataInfo.put("uid", message.getUid());
                dataInfo.put("point", randomNumber);
                dataInfo.put("command", "rima_message");

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://wx-api.suitwe.com/api/point/deduction/command"))
                        .timeout(Duration.ofSeconds(20))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(dataInfo)))
                        .build();

                String body = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
                log.info("Response body: {}", body);
                return WxAssistantResponse.text(message.getGid(), "⚠黄牌警告：RI-MA卫士发现群友 " + message.getUsername() + " 说RIMA，扣除积分" + randomNumber + "分！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Boolean convertToPinyin(String str) {
        String temp = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (isChinese(c)) {
                // 将中文字符转换为拼音数组
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);

                // 获取拼音的第一个音节，并去除声调
                if (pinyinArray != null && pinyinArray.length > 0) {
                    String res = pinyinArray[0].replaceAll("\\d", "");
                    temp += res;
                    if (temp.equals("rima") || temp.equals("rini") ||
                            temp.equals("caoni") || temp.equals("wori") || temp.equals("nima")) {
                        return true;
                    } else {
                        temp = res;
                    }
                } else {
                    temp += c;
                    if (temp.equals("rima") || temp.equals("rini") ||
                            temp.equals("caoni") || temp.equals("wori") || temp.equals("nima")) {
                        return true;
                    } else {
                        temp = String.valueOf(c);
                    }
                }
            }
        }
        return false;
    }

    public static boolean isChinese(char c) {
        // 根据Unicode编码判断字符是否为中文字符
        return Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN;
    }
}
