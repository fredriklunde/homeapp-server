package com.bernerus.other.presentation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * Created by andreas on 2017-05-04.
 */
public class OneLinerSort {

  public class Person {
    private String givenName;
    private String surName;
    private int age;
    private String gender;
    private String eMail;
    private String phone;
    private String address;

    public Person(String givenName, String surName) {
      this.givenName = givenName;
      this.surName = surName;
    }
  }

  @Test
  public void stream() throws Exception {
    List<Person> bestFriendsForever = new ArrayList<>();
    bestFriendsForever.add(new Person("Andreas", "Bernérus"));
    bestFriendsForever.add(new Person("Jason", "Statham"));
    bestFriendsForever.add(new Person("Scarlet", "Johanson"));
    bestFriendsForever.add(new Person("Dwayne", "Johnson"));
    bestFriendsForever.add(new Person("Alicia", "Vikander"));
    //For the moneys
    bestFriendsForever.add(new Person("Mark", "Zuckerberg"));

    // OneLiner here :)

    // To list of names

    // Joining names
  }

  private void print(long l) {
    System.out.println(l);
  }

  private void print(String s) {
    System.out.println(s);
  }
}
