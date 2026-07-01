package com.bond.librarymanager.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventCreationRequest {

    private String name;
    private String description;
    private LocalDate date;
}