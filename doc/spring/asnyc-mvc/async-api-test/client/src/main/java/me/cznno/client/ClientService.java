package me.cznno.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "name", url = "http://localhost:9001/foo")
interface ClientService {
    @GetMapping
    List<String> get();

    @GetMapping("timeout")
    List<String> getTimeout();
}