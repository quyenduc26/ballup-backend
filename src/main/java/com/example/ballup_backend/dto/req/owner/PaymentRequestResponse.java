package com.example.ballup_backend.dto.req.owner;


import java.sql.Timestamp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequestResponse {
    Long id;
    Long amount;
    String creator;
    Timestamp createdAt;
}



