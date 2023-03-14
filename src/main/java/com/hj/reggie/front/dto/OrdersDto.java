package com.hj.reggie.front.dto;

import com.hj.reggie.front.bean.OrderDetail;
import com.hj.reggie.front.bean.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
