package week0802.week0802;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import week0802.week0802.mappers.OrderMapper;
import week0802.week0802.models.Order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@MapperScan("week0802.week0802.mappers")
public class Week0802Application  implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private OrderMapper orderMapper;

    public static void main(String[] args) {
        SpringApplication.run(Week0802Application.class, args);
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
            //通过shardingsphere-proxy插入数据
        Order order1 = new Order( "1", 111, 111, new BigDecimal(1.1), 1, "插入数据1", 1);
        Order order2 = new Order( "2", 222, 222, new BigDecimal(2.2), 2, "插入数据2", 1);
        orderMapper.insertOne(order1);
        orderMapper.insertOne(order2);

        //通过shardingsphere-proxy查询数据
        Map<String, Object> queryCondition = new HashMap<>(1);

        queryCondition.put("user_id", 111);
        List<Map<String, Object>> orderData = orderMapper.query(queryCondition);
        for (Map item: orderData) {
            System.out.println(item.toString());
        }
        //删除数据
        orderMapper.deleteData(543568338853801984l);

    }
}
