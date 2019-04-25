package me.cznno.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author cznno
 * Date: 2019/2/19
 */
@SuppressWarnings("Duplicates")
@RestController
public class ClientController {
    private final ClientService clientService;

    Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    public ClientController(ClientService clientService) {this.clientService = clientService;}

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        System.out.println("aaaaa");
        Future<?> foo = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("foo");
        });
        System.out.println("bbbb");
        foo.get();
    }

    @GetMapping("foo/sync")
    public List<String> getSync() throws ExecutionException, InterruptedException {
        Future<List<String>> result = executorService.submit(clientService::get);
        return result.get();
    }

    @GetMapping("foo/async")
    public DeferredResult<List<String>> getAsync() {
        DeferredResult<List<String>> deferredResult = new DeferredResult<>();
        logger.info("parent thread is " + Thread.currentThread().getName());
        logger.info("async start");
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("execute thread is " + Thread.currentThread().getName());
            return clientService.getTimeout();
        })
                         .whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        logger.info("async end");
        return deferredResult;
    }

    @GetMapping("foo/asyncTimout")
    public DeferredResult<List<String>> getAsyncTimeout() {
        DeferredResult<List<String>> deferredResult = new DeferredResult<>(3000L);
        deferredResult.onTimeout(() -> deferredResult.setResult(Collections.singletonList("timeout")));
        logger.info("parent thread is " + Thread.currentThread().getName());
        logger.info("async start    ");
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("execute thread is " + Thread.currentThread().getName());
            return clientService.getTimeout();
        })
                         .whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        logger.info("async end");
        return deferredResult;
    }

    @GetMapping("foo/asyncWithCallable")
    public Callable<List<String>> getAsyncWithCallable() {
        System.out.println("parent thread is " + Thread.currentThread().getName());
        return () -> {
            System.out.println("execute thread is " + Thread.currentThread().getName());
            return clientService.getTimeout();
        };
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public Map<String, String> timeoutHandler() {
        HashMap<String, String> map = new HashMap<>();
        map.put("result", "timeout");
        return map;
    }
}
