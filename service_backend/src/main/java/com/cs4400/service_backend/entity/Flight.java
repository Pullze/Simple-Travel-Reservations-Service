package com.cs4400.service_backend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Flight {
    @ApiModelProperty(value = "flight_num")
    private String Flight_Num;

    @ApiModelProperty(value = "airline_name")
    private String Airline_Name;

    @ApiModelProperty(value = "departure_time")
    private Date Departure_Time;

    @ApiModelProperty(value = "arrival_time")
    private Date Arrival_Time;

    @ApiModelProperty(value = "flight_date")
    private Date Flight_Date;

    @ApiModelProperty(value = "cost")
    private double Cost;

    @ApiModelProperty(value = "capacity")
    private int Capacity;

    @ApiModelProperty(value = "from_airport")
    private String From_Airport;

    @ApiModelProperty(value = "to_airport")
    private String To_Airport;


}
