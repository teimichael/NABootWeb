package stu.napls.nabootweb.auth.request;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import stu.napls.nabootweb.auth.model.AuthResponse;
import stu.napls.nabootweb.config.property.AuthServer;

import javax.annotation.Resource;

/**
 * @Author Tei Michael
 * @Date 12/29/2019
 */
@Component
public class AuthRequest {

    @Resource
    private AuthServer authServer;

    private static final String LOGIN = "/auth/login";
    private static final String LOGOUT = "/auth/logout";
    private static final String REGISTER = "/auth/register";
    private static final String VERIFY = "/verify";

    private RestTemplate restTemplate = new RestTemplate();

    public AuthResponse login(String username, String password) {
        ResponseEntity<AuthResponse> responseEntity = restTemplate
                .exchange(authServer.getUrl() + LOGIN, HttpMethod.POST, getHttpEntity(getMapForLogin(username, password)), AuthResponse.class);
        return responseEntity.getBody();
    }

    public AuthResponse register(String username, String password) {
        ResponseEntity<AuthResponse> responseEntity = restTemplate
                .exchange(authServer.getUrl() + REGISTER, HttpMethod.POST, getHttpEntity(getMapForRegister(username, password)), AuthResponse.class);
        return responseEntity.getBody();
    }

    public AuthResponse logout(String token) {
        ResponseEntity<AuthResponse> responseEntity = restTemplate
                .exchange(authServer.getUrl() + LOGOUT, HttpMethod.POST, getHttpEntity(getMapForLogout(token)), AuthResponse.class);
        return responseEntity.getBody();
    }

    public AuthResponse verify(String token) {
        ResponseEntity<AuthResponse> responseEntity = restTemplate
                .exchange(authServer.getUrl() + VERIFY, HttpMethod.POST, getHttpEntity(getMapForVerify(token)), AuthResponse.class);
        return responseEntity.getBody();
    }

    public static MultiValueMap<String, String> getMapForLogin(String username, String password) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("username", username);
        multiValueMap.add("password", password);
        return multiValueMap;
    }

    public static MultiValueMap<String, String> getMapForRegister(String username, String password) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("username", username);
        multiValueMap.add("password", password);
        return multiValueMap;
    }

    public static MultiValueMap<String, String> getMapForLogout(String uuid) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("uuid", uuid);
        return multiValueMap;
    }

    public static MultiValueMap<String, String> getMapForVerify(String token) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("token", token);
        return multiValueMap;
    }

    private static HttpEntity<MultiValueMap<String, String>> getHttpEntity(MultiValueMap<String, String> multiValueMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(multiValueMap, headers);
    }
}
