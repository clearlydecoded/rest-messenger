package com.clearlydecoded.commander.example.service;

import static org.junit.Assert.assertEquals;

import com.clearlydecoded.commander.example.model.Cookie;
import java.util.List;
import org.junit.Test;

public class CookieStoreServiceTest {

  private CookieStoreService cookieStoreService = new CookieStoreService();

  @Test
  public void testGiveMeSugarComaCookie() {
    Cookie mostSugaryCookie = cookieStoreService.giveMeSugarComaCookie();
    Cookie expectedCookie = new Cookie("Sugar Sugar", 300,
        new String[]{"sugar", "more sugar", "even more sugar", "diabetes"});

    assertEquals("Should be the most sugar value cookie", expectedCookie, mostSugaryCookie);
  }

  @Test
  public void testGiveMeFirstOneWithAdditionOfGinger() {
    Cookie gingerCookie = cookieStoreService.giveMeFirstOneWithAdditionOf("Ginger");
    Cookie expectedCookie = new Cookie("Ginger", 60, new String[]{"ginger", "sugar"});

    assertEquals("Should be the first ginger cookie.", expectedCookie, gingerCookie);
  }

  @Test
  public void testGiveMeCookiesUpToMaxSugar115() {
    List<Cookie> cookies = cookieStoreService.giveMeCookiesUpToMaxSugar(115);

    assertEquals("Should return 4 cookies", 4, cookies.size());
  }

  @Test
  public void testGiveMeCookiesUpToMaxSugar845() {
    List<Cookie> cookies = cookieStoreService.giveMeCookiesUpToMaxSugar(845);

    assertEquals("Should return 12 cookies", 12, cookies.size());
  }
}
