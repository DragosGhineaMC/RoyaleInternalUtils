package com.dragosghinea.royale.internal.utils.messages.impl;

import com.dragosghinea.royale.internal.utils.messages.ComponentMessageMapper;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ComponentMessageMapperImpl implements ComponentMessageMapper {

    public String mapComponentToString(BaseComponent[] component) {
        return ComponentSerializer.toString(component);
    }

    public BaseComponent[] mapStringToComponent(String message) {
        return ComponentSerializer.parse(message);
    }
}
