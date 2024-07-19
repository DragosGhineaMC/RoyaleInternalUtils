package com.dragosghinea.royale.internal.utils.messages;

import net.md_5.bungee.api.chat.BaseComponent;

public interface ComponentMessageMapper {

    String mapComponentToString(BaseComponent[] component);

    BaseComponent[] mapStringToComponent(String message);
}
