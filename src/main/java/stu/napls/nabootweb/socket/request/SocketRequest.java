package stu.napls.nabootweb.socket.request;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import stu.napls.nabootweb.config.property.SocketServer;
import stu.napls.nabootweb.socket.model.SocketRegister;
import stu.napls.nabootweb.socket.model.SocketResponse;

import javax.annotation.Resource;

@Component
public class SocketRequest {

    @Resource
    private SocketServer socketServer;

    private static final String REGISTER = "/access/register";

    private RestTemplate restTemplate = new RestTemplate();

    public SocketResponse register(SocketRegister socketRegister) {
        return restTemplate.postForObject(socketServer.getUrl() + REGISTER, socketRegister, SocketResponse.class);
    }
}
