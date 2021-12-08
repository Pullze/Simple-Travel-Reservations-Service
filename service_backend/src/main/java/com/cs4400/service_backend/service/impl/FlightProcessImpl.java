package com.cs4400.service_backend.service.impl;

import com.cs4400.service_backend.entity.Airport;
import com.cs4400.service_backend.entity.Flight;
import com.cs4400.service_backend.mapper.FlightMapper;
import com.cs4400.service_backend.service.FlightProcess;
import com.cs4400.service_backend.vo.BookInfo;
import com.cs4400.service_backend.vo.FlightInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class FlightProcessImpl implements FlightProcess {

    @Resource
    private FlightMapper flightMapper;

    @Override
    public FlightInfo schedule_flight(FlightInfo flightInfo) {

        String flight_num = flightInfo.getFlight_num();
        String airline_name = flightInfo.getAirline_name();
        String from_airport = flightInfo.getFrom_airport();
        String to_airport = flightInfo.getTo_airport();
        Time departure_time = new Time(flightInfo.getDeparture_time().getTime());
        Time arrival_time = new Time(flightInfo.getArrival_time().getTime());
        Date flight_date = new Date(flightInfo.getFlight_date().getTime());
        double cost = flightInfo.getCost();
        int capacity = flightInfo.getCapacity();
        Date current_date = new Date(flightInfo.getCurrent_date().getTime());


        if (flight_date.compareTo(current_date) <= 0) {
            flightInfo.setMessage("Can not schedule a flight not in a future date!");
            return flightInfo;
        }

        if (flightMapper.check_airport(from_airport) == null) {
            flightInfo.setMessage("Can not departure from an airport that doesn't exist!");
            return flightInfo;
        }

        if (flightMapper.check_airport(to_airport) == null) {
            flightInfo.setMessage("Can not arrive at an airport that doesn't exist!");
            return flightInfo;
        }

        if (to_airport.equals(from_airport)) {
            flightInfo.setMessage("Can not schedule a flight from and to the same airport!");
            return flightInfo;
        }

        if (arrival_time.compareTo(departure_time) <= 0) {
            flightInfo.setMessage("arrival time is before departure time!");
            return flightInfo;
        }



        if ( flightMapper.check_flight(flight_num, airline_name) != null) {
            flightInfo.setMessage( "The combination of the flight number and the airline name already exists!");
            return flightInfo;
        } else {
            flightMapper.schedule_flight(flight_num, airline_name, from_airport , to_airport,
                    departure_time, arrival_time, flight_date, cost, capacity);
            flightInfo.setMessage("schedule succeeded!");
            return flightInfo;
        }

    }


    @Override
    public List<Flight> view_flight(int minSeats) {
        return flightMapper.view_flight(minSeats);
    }

    @Override
    public BookInfo book_flight(String flight_num, String airline_name, String customer_email, int num_seats) {
        if (flightMapper.check_book_cancelled(flight_num, airline_name, customer_email) != null) {
            return new BookInfo("Already cancelled a book on this flight!");
        }

        if (flightMapper.check_if_booked(flight_num, airline_name, customer_email) != null) {
            if (flightMapper.check_seats(flight_num, airline_name) < num_seats) {
                return new BookInfo("No sufficient seats to book!");
            } else{
                flightMapper.update_book(flight_num, airline_name, customer_email, num_seats);
                int total_cost = (int)flightMapper.total_cost(flight_num,airline_name,num_seats);
                Flight flight = flightMapper.check_flight(flight_num, airline_name);
            }

        }

        return null;

    }
}
