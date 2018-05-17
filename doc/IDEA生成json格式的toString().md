# IDEA生成json格式的toString()

```
public java.lang.String toString() {
return "{" +
    #set ($i = 0)
    #foreach ($member in $members)
    #if ($i == 0)
    "#else",#end#if ($member.string || $member.date)\"$member.name\":\""#else\"$member.name\":"#end    
    #if ($member.primitiveArray || $member.objectArray)
    + java.util.Arrays.toString($member.name) +
    #elseif ($member.string || $member.date)
    + $member.accessor + "\"" +
    #else
    + $member.accessor +
    #end
    #set ($i = $i + 1)
    #end
    "}";
}
```

