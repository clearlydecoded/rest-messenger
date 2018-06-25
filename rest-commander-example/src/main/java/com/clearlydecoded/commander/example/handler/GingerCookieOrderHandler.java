package com.clearlydecoded.commander.example.handler;

import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.example.command.GingerCookieOrder;
import com.clearlydecoded.commander.example.command.GingerCookieOrderResponse;
import com.clearlydecoded.commander.example.model.Cookie;
import com.clearlydecoded.commander.example.service.CookieStoreService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GingerCookieOrderHandler implements
    CommandHandler<GingerCookieOrder, GingerCookieOrderResponse> {

  @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
  @Autowired
  @Setter
  private CookieStoreService cookieStoreService;

  @Override
  public GingerCookieOrderResponse execute(GingerCookieOrder command) {
    Cookie gingerCookie = cookieStoreService.giveMeFirstOneWithAdditionOf("Ginger");
    return new GingerCookieOrderResponse(gingerCookie);
  }

  @Override
  public String getCompatibleCommandType() {
    return GingerCookieOrder.TYPE;
  }

  @Override
  public Class<GingerCookieOrder> getCompatibleCommandClassType() {
    return GingerCookieOrder.class;
  }
}
