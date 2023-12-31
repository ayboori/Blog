package com.example.hanghaeblog.jwt;

import com.example.hanghaeblog.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {
    // Header KEY 값 - Response 객체 Header에 바로 넣는 방법 / Token에 담아서 넣는 방법이 있음
    // Cookie를 만들 때는 Cookie의 Name 값이 된다.
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY (Admin, ... )
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자 / 이후에 오는 것은 Token이라고 알려주는 일종의 규칙
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey (application.properties에서 가져오는 법)
    private String secretKey; // 여기에 위의 secretKey 담아준다
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct // 한 번만 받아오면 되는 값을 설정할 때마다 새로 요청하는 실수를 방지하기 위해 사용됨
    // 생성자 호출 뒤에 이 코드가 호출됨
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); // secretKey Decoding
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 1. JWT 토큰 생성
    // 아래의 모든 값을 넣을 필요는 없다
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID) // PK 등의 다른 값을 넣어도 됨
                        .claim(AUTHORIZATION_KEY, role)     // 사용자 권한 (key, value)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘 (KEY, 알고리즘을 넣어서 암호화 시킴)
                        .compact();
    }

    // 2. 생성된 JWT를 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value(인코딩한 토큰)
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
    // 3. Cookie에 들어있던 JWT 토큰을 Substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) { // 공백이나 null이 아니고, bearer로 시작하는지
            return tokenValue.substring(7); // "bearer " = 7자 / 순수한 token 값만 return
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 4. JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // token의 위변조 / 만료 검사
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 5. JWT에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) { // JWT는 Claim 기반 Web Token
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 공백이나 null이 아니고, bearer로 시작하는지
            return bearerToken.substring(7); // "bearer " = 7자 / 순수한 token 값만 return
        }
        return null;
    }
}