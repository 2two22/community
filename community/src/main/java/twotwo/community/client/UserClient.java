package twotwo.community.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import twotwo.community.dto.response.UserResponse;

@FeignClient(name = "userClient", url = "${feign.user}")
public interface UserClient {
    @GetMapping(value = "/member")
    UserResponse getUserInfo(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token);
}
