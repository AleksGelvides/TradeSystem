package com.ts.obtaining_quotes.domain;

import com.ts.obtaining_quotes.enums.Status;
import lombok.AllArgsConstructor;

public record FacadeResponse (Status status, String message){
}
