package week0802.week0802.mappers;

import org.springframework.stereotype.Repository;
import week0802.week0802.models.Order;

import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Repository
public interface OrderMapper {

    List<Map<String, Object>> query(Map<String, Object> condition);
    void insertOne(Order order);
    void deleteData(long id);

}
