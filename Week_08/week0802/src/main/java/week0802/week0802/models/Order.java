package week0802.week0802.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author
 */

@Data
public class Order {

    private long order_id;
    private String order_no;
    private long user_id;
    private long business_id;
    private BigDecimal pay_amount;
    private int order_status;
    private String note;
    private int delete_status;
    private Date payment_time;
    private Date create_time;
    private Date update_time;

    public Order(String order_no, long user_id, long business_id, BigDecimal pay_amount, int order_status, String note, int delete_status) {
        this.order_no = order_no;
        this.user_id = user_id;
        this.business_id = business_id;
        this.pay_amount = pay_amount;
        this.order_status = order_status;
        this.note = note;
        this.delete_status = delete_status;
    }
}
