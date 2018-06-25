package com.clearlydecoded.commander.example.handler;

import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.example.command.MaxSugarOrder;
import com.clearlydecoded.commander.example.command.MaxSugarOrderResponse;
import com.clearlydecoded.commander.example.model.Cookie;
import com.clearlydecoded.commander.example.service.CookieStoreService;
import java.util.List;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaxSugarOrderHandler implements CommandHandler<MaxSugarOrder, MaxSugarOrderResponse> {

  @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
  @Autowired
  @Setter
  private CookieStoreService cookieStoreService;

  @Override
  public MaxSugarOrderResponse execute(MaxSugarOrder command) {
    List<Cookie> cookies = cookieStoreService
        .giveMeCookiesUpToMaxSugar(command.getMaxSugarInGrams());

    // Loop over all cookies and add up total sugar
    int totalSugar = 0;
    for (Cookie cookie : cookies) {
      totalSugar += cookie.getSugarInGrams();
    }

    return new MaxSugarOrderResponse(cookies, totalSugar);
  }

  @Override
  public String getCompatibleCommandType() {
    return MaxSugarOrder.TYPE;
  }

  @Override
  public Class<MaxSugarOrder> getCompatibleCommandClassType() {
    return MaxSugarOrder.class;
  }
}
