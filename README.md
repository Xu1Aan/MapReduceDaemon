## Java从零实现单点MapReduce :star2: 

> 感谢Star，项目问题交流可发[邮件](toxuan1998@qq.com)

学习Hadoop的MapReduce时，为能够更加充分的了解MapReduce底层原理，希望从零实现一个自己的MapReduce。而著名的MIT 6.824课程MapReduce由 Go 语言完成（**MIT  6.824** 分布式系统课程,是一门著名的讲解分布式系统设计原理的课程）。同样是服务端编程语言的Java，网上从零实现MapReduce资源却很少，如此看来，如果能有一个 Java 版的课程作业可供学习挑战，收益人应该还会更多。

本项目为单点MapReduce项目（非分布），后期将对此项目重构为分布式MapReduce项目。此项目实现了单点的wordcount案例。

该项目由三部分组成（结构）：

common: `KeyValue 类`；

handler：`MapReduce接口 `、` WordCount实现类` ；

main： `MapReduceDaemon程序入口 `。

*程序入口：*com.xu1an.MapReduceDaemon，运行main方法。需要指定文件夹路径`filePath`以及Map Reduce接口的实现类的全类名`className`。

```java
public static void main(String[] args) {
	start("./data","com.xu1an.handler.impl.WordCount");
}
```

**执行流程:smile:**

- Map：读取文件夹（./data），调用map方法，通过KeyValue记录出现的所有单词，将所有的KeyValue添加到List集合中。最后对List按key排序
- Refuce： 准备写出文件（mr-out.txt）,变量List集合，将相同key进行计数，计入输出文件。

**流程图**

![](.\picture\流程介绍.png)

Splitting代码：

```java
// 对应Splitting阶段
List<String> words = Arrays.stream(contents.split("[^a-zA-Z]+"))
    .filter(word -> !"".equals(word))
    .collect(Collectors.toList());
```

Mapping代码：

```java
//对应Mapping阶段
List<KeyValue> keyValues = new ArrayList<>(words.size());
 for (String word : words) {
    KeyValue keyValue = new KeyValue(word, "1");
    keyValues.add(keyValue);
}
```

```java
//将mapping后的KeyValue数据放入List集合中（intermediate）
for (File f : files) {
    String content = readAll(f);
    intermediate.addAll(mapReduce.map(content));
}
```

shuffling、reduce代码：

```java
for (int i = 0; i < intermediate.size(); ) {
    //shuffling 阶段
    int j = i + 1;
    String keyI = intermediate.get(i).getKey();
    while (j < intermediate.size() && (intermediate.get(j).getKey().equals(keyI))) {
    	j++;
    }
    List<String> values = new ArrayList<>(j - i);
    for (int k = i; k < j; k++) {
    	values.add(intermediate.get(k).getValue());
    }
    
    //reduce 阶段
    String output = mapReduce.reduce(values); 
    
    //result
    bw.write(keyI + " " + output + System.lineSeparator());
    i = j;
}
```

```java
// Reduce方法
return String.valueOf(values.size());
```

**参考资料:tada:**

[MIT6.824课程]( https://pdos.csail.mit.edu/6.824/labs/lab-mr.html)

 [MIT6.824-Java](https://github.com/razertory/MIT6.824-Java)

[尚硅谷大数据资料](https://space.bilibili.com/302417610?spm_id_from=333.337.0.0)



