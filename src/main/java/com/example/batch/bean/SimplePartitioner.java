package com.example.batch.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SimplePartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> result = new HashMap<>();

        /** 데이터 분배. 0~100 의 데이터를 gridSize 만큼 나눠서 분배.
         *  gridSize 가 3이라면 3번의 step 을 호출.
         *
         * */
        int start = 0;
        int end = 100;
        int dataSize = (end - start) / gridSize + 1;

        int startPos = start;
        int endPos = startPos + dataSize - 1;
        int num = 1;
        while (startPos <= end) {
            log.info("before data distribution,  startPos : {} , endPos : {}", startPos, endPos);
            ExecutionContext executionContext = new ExecutionContext();
            result.put("part" + num, executionContext);

            /** endPos 이 end 를 넘어설 경우 처리 */
            if(endPos >= end) {
                endPos = end;
            }

            executionContext.putInt("start", startPos);
            executionContext.putInt("end", endPos);


            startPos += dataSize;
            endPos += dataSize;

            num++;

            log.info("after data distribution,  startPos : {} , endPos : {}", startPos, endPos);
        }
        return result;
    }
}
