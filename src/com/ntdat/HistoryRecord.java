package com.ntdat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class HistoryRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate date;
    private HashMap<String, Integer> record;

    public HistoryRecord(LocalDate date, HashMap<String, Integer> record) {
        this.date = date;
        this.record = record;
    }

    public LocalDate getDate() {
        return date;
    }

    public HashMap<String, Integer> getRecord() {
        return record;
    }
}
