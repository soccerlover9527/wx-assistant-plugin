package com.soccerlover9527.wxassistant.plugin.base;

import com.soccerlover9527.wxassistant.plugin.base.core.message.CommandMessage;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.base.event.BaseEvent;
import com.soccerlover9527.wxassistant.plugin.base.event.CommandEvent;
import com.soccerlover9527.wxassistant.plugin.base.event.MessageEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author soccerlover9527@gmail.com
 */
@Component
public class InboundMessageListener implements SmartApplicationListener {
    private final HandlerContext handlerContext;
    private final MessageDispatcher messageDispatcher;

    public InboundMessageListener(HandlerContext handlerContext, MessageDispatcher messageDispatcher) {
        this.handlerContext = handlerContext;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType.isAssignableFrom(MessageEvent.class)  || eventType.isAssignableFrom(CommandEvent.class);
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        var baseEvent = (BaseEvent) event;
        var clientName = baseEvent.getClientName();
        if (baseEvent instanceof MessageEvent) {
            //  handle message
            var messageHandlers = handlerContext.getMessageHandlers(clientName);
            for (IWxMessageHandler messageHandler : messageHandlers) {
                var response = messageHandler.onMessage((Message) baseEvent.getSource());
                if (response != null) {
                    messageDispatcher.dispatch(clientName, response);
                }
            }
        } else {
            CommandEvent commandEvent = (CommandEvent) baseEvent;
            //  handle command
            var commandHandlers = handlerContext.getCommandHandlers(clientName);
            for (IWxCommandHandler commandHandler : commandHandlers) {
                //  get handler support commands
                List<String> supportCommands = commandHandler.supportCommands();
                CommandMessage command = (CommandMessage) commandEvent.getSource();
                //  handle command if supported.
                if (supportCommands.contains(command.getCommand())) {
                    var response = commandHandler.onMessage((CommandMessage) commandEvent.getSource());
                    if (response != null) {
                        messageDispatcher.dispatch(clientName, response);
                    }
                }
            }
        }
    }
}
