package com.croco.interview.management.order.config;


import com.croco.interview.management.order.model.response.OrdersResponse;
import com.croco.interview.management.order.model.response.PageableResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class PageableOrderEntitySerializer implements StreamSerializer<PageableResponse<OrdersResponse>> {

    private final ObjectMapper objectMapper;

    @Override
    public void write(ObjectDataOutput objectDataOutput, PageableResponse<OrdersResponse> ordersResponsePageableResponse) throws IOException {
        objectDataOutput.writeString(objectMapper.writeValueAsString(ordersResponsePageableResponse));
    }

    @Override
    public PageableResponse<OrdersResponse> read(ObjectDataInput objectDataInput) throws IOException {
        return objectMapper.readValue(objectDataInput.readString(), PageableResponse.class);
    }

    @Override
    public int getTypeId() {
        return 13;
    }
}
