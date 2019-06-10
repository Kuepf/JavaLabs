package com.client.demo;

import com.common.CreatorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceAPI {
    private final ServiceClient serviceClient;
    @GetMapping("/creators")
    public List<CreatorDTO> creators() {
        return serviceClient.GetCreators();
    }

}