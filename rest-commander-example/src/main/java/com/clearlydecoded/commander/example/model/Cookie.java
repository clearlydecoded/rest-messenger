package com.clearlydecoded.commander.example.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cookie implements Serializable {

  private String name;

  private int sugarInGrams;

  private String[] additions;
}
