package com.cs4400.service_backend.controller;

import com.cs4400.service_backend.entity.Response;
import com.cs4400.service_backend.service.BookProcess;
import com.cs4400.service_backend.vo.BookInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api (tags = "Book Controller")

@RestController
public class BookController {

    @Autowired
    private BookProcess bookProcess;

    @PostMapping(value = "/book_flight")
    @ApiModelProperty(value = "book flight", notes = "book flight")
    public Response<BookInfo> book_flight(@RequestBody BookInfo bookInfo) {

        BookInfo returnBookInfo = bookProcess.book_flight(bookInfo);

        Response<BookInfo> response = new Response<>(bookInfo.getBook_message(), returnBookInfo);

        if (response.getMessage() == "Succeeded updating booking on this flight!" ||
            response.getMessage() == "Succeeded booking this flight")  {
            response.setCode(200);
        } else {
            response.setCode(400);
        }

        return response;
    }
}
