package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private String id;
    HashMap<String, Order> order;
    HashMap<String, DeliveryPartner> partnerMap;
    HashMap<String, List<String>> orderPartnerPairMap;
    HashSet<String> unassignedOrderMap;

    public OrderRepository() {
        this.id = "1";
        this.order = new HashMap<>();
        this.partnerMap= new HashMap<>();
        this.orderPartnerPairMap= new HashMap<>();
        this.unassignedOrderMap = new HashSet<>();
    }

    public void addTheOrder(Order order) {
        order.setId(this.id);
        this.id = Integer.toString(Integer.parseInt(this.id) + 1);
        this.order.put(order.getId(), order);
        this.unassignedOrderMap.add(order.getId());
    }

    public void addThePartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(order.containsKey(orderId) && partnerMap.containsKey(partnerId) && unassignedOrderMap.contains(orderId)) {
            List<String> listOfOrders = new ArrayList<>();

            if(orderPartnerPairMap.containsKey(partnerId)) {
                listOfOrders = orderPartnerPairMap.get(partnerId);
            }
            listOfOrders.add(orderId);
            orderPartnerPairMap.put(partnerId, listOfOrders);
            unassignedOrderMap.remove(orderId);
        }
    }

    public Order getOrderById(String orderId) {
        return order.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public Integer gettheOrderCountByPartnerId(String partnerId) {
        return partnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> listOfOrder = new ArrayList<>();

        if(orderPartnerPairMap.containsKey(partnerId)) {
            List<String> listOfOrderId = orderPartnerPairMap.get(partnerId);

            for (String orderId : listOfOrderId) {
                listOfOrder.add(order.get(orderId).toString());
            }
        }
        return listOfOrder;
    }

    public List<String> getAlltheOrders() {
        List<String> listOfOrder = new ArrayList<>();

        for(String orderId : order.keySet()) {
            listOfOrder.add(order.get(orderId).toString());
        }
        return listOfOrder;
    }

    public Integer getCountOfUnassignedOrders() {
        return unassignedOrderMap.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {

        Integer orderCount = 0;
        List<String> listOfOrders = orderPartnerPairMap.get(partnerId);

        for(String orderId : listOfOrders) {
            if(order.get(orderId).getDeliveryTime() > Integer.parseInt(time)) {
                orderCount++;
            }
        }
        return orderCount;
    }

    public String gettheLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> listOfOrders = orderPartnerPairMap.get(partnerId);

        int lastDeliveryTime = Integer.MIN_VALUE;
        for(String orderId : listOfOrders) {
            if(order.get(orderId).getDeliveryTime() > lastDeliveryTime) {
                lastDeliveryTime = order.get(orderId).getDeliveryTime();
            }
        }
        return Integer.toString(lastDeliveryTime);
    }

    public void deletethePartnerById(String partnerId) {
        if(orderPartnerPairMap.containsKey(partnerId)) {
            List<String> listOfOrders = orderPartnerPairMap.get(partnerId);

            for (String orderId : listOfOrders) {
                unassignedOrderMap.add(order.get(orderId).getId());
            }
            orderPartnerPairMap.remove(partnerId);
        }
        partnerMap.remove(partnerId);
    }

    public void deletetheOrderById(String orderId) {
        order.remove(orderId);

        for(List<String> orderIds : orderPartnerPairMap.values()) {

            for(String order : orderIds) {
                if(order.equals(orderId)) {
                    orderIds.remove(orderId);
                    return;
                }
            }
        }
    }

}