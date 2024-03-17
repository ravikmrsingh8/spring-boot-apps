package com.example.cookie.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

@RestController
public class HelloController {
    public final String SauronRising = """
        Now Sauron's lust and pride increased, until he knew no bounds, and he determined to make himself master of all things in Middle-earth, 
        and to destroy the Elves, and to compass if he might, the downfall of Númenor.   
    """;
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ResponseEntity<String> greet() {
        ResponseCookie cookie1 = ResponseCookie.from("user-id", UUID.randomUUID().toString())
                        .maxAge(Duration.ofSeconds(3600)).build();
        ResponseCookie cookie2 = ResponseCookie.from("user-name", "Sauron")
                .maxAge(Duration.ofSeconds(3600)).build();
        ResponseCookie cookie3 = ResponseCookie.from("hunting-for", "MasterRing!") // Cookie value cannot have Space. Strange!
                .maxAge(Duration.ofSeconds(3600)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());

        return new ResponseEntity<>(SauronRising, headers, HttpStatus.OK);
    }

    @GetMapping("/destroy")
    public ResponseEntity<String> logout() {
        ResponseCookie cookie1 = ResponseCookie.from("user-id").maxAge(Duration.ZERO).build();
        ResponseCookie cookie2 = ResponseCookie.from("user-name").maxAge(Duration.ZERO).build();
        ResponseCookie cookie3 = ResponseCookie.from("hunting-for").maxAge(Duration.ZERO).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie1.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie2.toString());
        headers.add(HttpHeaders.SET_COOKIE, cookie3.toString());
        return new ResponseEntity<>("Master Ring Destroyed, so is Sauron!", headers, HttpStatus.OK);
    }


    @GetMapping("/return")
    public String readCookie(@CookieValue(name="user-id", defaultValue = "") String userId,
                             @CookieValue(name="user-name", defaultValue = "") String userName) {
        if (!userName.isEmpty() && !userId.isEmpty()) {
            return userName +"(" + userId +")"+", I see you!";
        }
        return "You do not exist!";
    }

    @GetMapping("return-all")
    public Cookie[] readAllCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            Arrays.stream(cookies).forEach(System.out::println);
        }
        return cookies;
    }
}
