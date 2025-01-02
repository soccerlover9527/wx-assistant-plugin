package com.soccerlover9527.wxassistant.plugin.base;

import com.soccerlover9527.wxassistant.plugin.base.annotation.WxAssistantPlugin;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author soccerlover9527@gmail.com
 */
@Component
public class HandlerContext {
    private final Map<String, IWxMessageHandler> wxMessageHandlers;
    private final Map<String, IWxCommandHandler> wxCommandHandlers;
    private final Map<String, Set<String>> clientActivePluginsMap = new ConcurrentHashMap<>();

    public HandlerContext(List<IWxMessageHandler> wxMessageHandlers, List<IWxCommandHandler> wxCommandHandlers) {
        //  mapping by plugin name
        this.wxMessageHandlers = wxMessageHandlers.stream().collect(Collectors.toMap(h -> {
            WxAssistantPlugin annotation = h.getClass().getAnnotation(WxAssistantPlugin.class);
            if (annotation == null) {
                throw new RuntimeException("WxAssistantPlugin annotation is required");
            }
            return annotation.value();
        }, Function.identity()));
        this.wxCommandHandlers = wxCommandHandlers.stream().collect(Collectors.toMap(h -> {
            WxAssistantPlugin annotation = h.getClass().getAnnotation(WxAssistantPlugin.class);
            if (annotation == null) {
                throw new RuntimeException("WxAssistantPlugin annotation is required");
            }
            return annotation.value();
        }, Function.identity()));
    }

    public void registerClientHandlers(String clientName, String[] activePlugins) {
        if (activePlugins == null || activePlugins.length == 0) {
            return;
        }
        clientActivePluginsMap.put(clientName, Set.copyOf(List.of(activePlugins)));
    }

    public List<IWxMessageHandler> getMessageHandlers(String clientId) {
        var activePlugins = clientActivePluginsMap.get(clientId);
        if (activePlugins == null || activePlugins.isEmpty()) {
            return wxMessageHandlers.values().stream().toList();
        }
        return activePlugins.stream().map(wxMessageHandlers::get).toList();
    }

    public List<IWxCommandHandler> getCommandHandlers(String clientId) {
        var activePlugins = clientActivePluginsMap.get(clientId);
        if (activePlugins == null) {
            return wxCommandHandlers.values().stream().toList();
        }
        return activePlugins.stream().map(wxCommandHandlers::get).toList();
    }
}
