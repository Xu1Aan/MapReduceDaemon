package com.xu1an;

import com.xu1an.common.KeyValue;
import com.xu1an.handler.MapReduce;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2022/07/11/20:13
 * @Description:
 */
@Slf4j
public class MapReduceDaemon {

    public static void main(String[] args) {
        start("./data","com.xu1an.handler.impl.WordCount");
    }

    /**
     * MapReduce Daemon 启动方法
     * @param filePath 执行路径
     * @param className 实现方法全类名
     */
    private static void start(String filePath, String className){

        log.debug("work file path: [{}], and mapreduce instance: [{}]", filePath, className);
        // 1. 判断文件路径是否有文件
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null) {
            log.error("the file path error. no file in dir");
            return;
        }

        // 2.执行map 读取文件内容，将内容分割，并用KeyValue对象保存，然后放入List集合
        log.debug("Map start");
        List<KeyValue> intermediate = new ArrayList<>(files.length);
        MapReduce mapReduce = getMapReduce(className);
        for (File f : files) {
            String content = readAll(f);
            intermediate.addAll(mapReduce.map(content));
        }
        log.debug("Map finished");


        // 3. 对List集合（相当于缓存区）排序，将数据按Key进行排序
        intermediate.sort(Comparator.comparing(KeyValue::getKey));

        String outputName = "mr-out.txt";
        File outputFile = new File(outputName);
        log.debug("the output will in :[{}]", outputName);


        log.debug("Reduce start");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < intermediate.size(); ) {
                int j = i + 1;
                String keyI = intermediate.get(i).getKey();

                while (j < intermediate.size() && (intermediate.get(j).getKey().equals(keyI))) {
                    j++;
                }
                List<String> values = new ArrayList<>(j - i);
                for (int k = i; k < j; k++) {
                    values.add(intermediate.get(k).getValue());
                }
                String output = mapReduce.reduce(values);
                bw.write(keyI + " " + output + System.lineSeparator());
                i = j;
            }
            log.debug("Reduce finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获得MapReduce对象,(分布式MapRecue做准备)
     * @param className 所需 MapReduce 类的完类名 （接口实现类）
     * @return MapReduce
     */
    private static MapReduce getMapReduce(String className) {
        MapReduce mapReduce;
        try {
            @SuppressWarnings("unchecked")
            Class<MapReduce> clazz = (Class<MapReduce>) Class.forName(className);
            mapReduce = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mapReduce;
    }


    /**
     * 实现对文件按行读入
     * @param file 文件路径
     * @return String 文件所有的内容
     */
    private static String readAll(final File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
