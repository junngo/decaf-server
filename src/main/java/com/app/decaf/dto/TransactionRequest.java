package com.app.decaf.dto;

import com.app.decaf.model.Entry;
import com.app.decaf.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class TransactionRequest {
    private Transaction transaction;
    private List<Entry> entries;
}
