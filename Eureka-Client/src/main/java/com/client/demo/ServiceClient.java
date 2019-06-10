package com.client.demo;

import com.common.CreatorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient (serviceId = "greetings-service")
public interface ServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/creators")
    List<CreatorDTO> GetCreators();

}
