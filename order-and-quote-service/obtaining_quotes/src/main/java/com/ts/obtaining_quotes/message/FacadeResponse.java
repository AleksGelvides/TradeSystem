package com.ts.obtaining_quotes.message;

import com.ts.obtaining_quotes.enums.Status;

public record FacadeResponse (Status status, String message){}
