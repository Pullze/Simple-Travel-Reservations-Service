package com.cs4400.service_backend.service;

import com.cs4400.service_backend.entity.Property;
import com.cs4400.service_backend.entity.Reserve;
import com.cs4400.service_backend.vo.ReserveInfo;

import java.util.List;

public interface PropertyProcess {

    List<Property> viewProperties(Integer high, Integer low);
    List<Property> viewAvailableProperties(String start, String end);
    String reserveProperty(Reserve reserve);
    List<ReserveInfo> viewCustomerFutureReservations(String customerEmail);
    String cancelPropertyReservation(String propertyName, String ownerEmail, String customerEmail);

}
