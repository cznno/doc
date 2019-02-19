package me.cznno.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@SpringBootApplication
@EnableFeignClients
@RestController
public class ClientApplication {
    @Autowired
    ClientService clientService;

    ExecutorService executorService = Executors.newFixedThreadPool(20);

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @GetMapping("foo/sync")
    public List<String> getSync() throws ExecutionException, InterruptedException {
        Future<List<String>> result = executorService.submit(() -> clientService.get());
        return result.get();
    }

    @GetMapping("foo/async")
    public DeferredResult<List<String>> getAsync() throws ExecutionException, InterruptedException {
        DeferredResult<List<String>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> clientService.get())
                         .whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    @GetMapping("foo/asyncTimout")
    public DeferredResult<List<String>> getAsyncTimeout() throws Exception {
        DeferredResult<List<String>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> clientService.getTimeout())
                         .whenCompleteAsync((result, throwable) -> deferredResult.setResult(result))
                         .get(3, TimeUnit.SECONDS);
        return deferredResult;
    }

    @ExceptionHandler(TimeoutException.class)
    public Map<String, String> timeoutHandler() {
        HashMap<String, String> map = new HashMap<>();
        map.put("result", "timeout");
        return map;
    }
}

