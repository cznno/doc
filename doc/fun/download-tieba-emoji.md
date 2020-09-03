```kotlin
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    for (i in (1..99)) {
        val execute = OkHttpClient().newCall(Request.Builder()
            .url("https://gsp0.baidu.com/5aAHeD3nKhI2p27j8IqW0jdnxx1xbK/tb/editor/images/client/image_emoticon$i.png")
            .build())
            .execute()
        val inputStream = execute.body!!.byteStream()

        Files.copy(inputStream, Paths.get("${i}.png"))
    }
}
```
