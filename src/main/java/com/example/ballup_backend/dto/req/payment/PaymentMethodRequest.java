package com.example.ballup_backend.dto.req.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentMethodRequest {
    
    @NotBlank
    private String name;

    @NotBlank
    private String bankName;

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String accountHolderName;

    private String bankBranch;

    @NotBlank
    private String qrImageUrl;

    private String instructions;

    @NotNull
    private Boolean isActive;
}
