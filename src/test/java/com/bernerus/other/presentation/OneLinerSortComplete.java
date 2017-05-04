package com.bernerus.other.presentation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andreas on 2017-05-04.
 */
public class OneLinerSortComplete {

  public class Person {
    private String givenName;
    private String surName;
    private int age;
    private String gender;
    private String eMail;
    private String phone;
    private String address;

    public Person() {
      this("Untitled", "", null);
    }

    public Person(String givenName, String surName, String gender) {
      this.givenName = givenName;
      this.surName = surName;
      this.gender = gender;
    }

    public String getGivenName() {
      return givenName;
    }

    public String getSurName() {
      return surName;
    }

    public void printMyName() {
      System.out.println(this.givenName + " " + this.surName);
    }

    public String getFullName() {
      return givenName + " " + surName;
    }
  }

  @Test
  public void stream() throws Exception {
    List<Person> bestFriendsForever = new ArrayList<>();
    bestFriendsForever.add(new Person("Andreas", "Bernérus", "Male"));
    bestFriendsForever.add(new Person("Jason", "Statham", "Male"));
    bestFriendsForever.add(new Person("Scarlet", "Johanson", "Female"));
    bestFriendsForever.add(new Person("Dwayne", "Johnson", "Male"));
    bestFriendsForever.add(new Person("Alicia", "Vikander", "Female"));
    //For the moneys
    bestFriendsForever.add(new Person("Mark", "Zuckerberg", "Male"));

    // Code this:
    //bestFriendsForever.stream().sorted((friend1, friend2) -> friend1.givenName.compareTo(friend2.givenName)).forEach(friend -> System.out.println(friend.givenName));

    //Improve to this:
    bestFriendsForever.stream().sorted(byName()).forEach(Person::printMyName);

    List<String> names = bestFriendsForever.stream().sorted(byName()).map(Person::getFullName).collect(Collectors.toList());

    print(bestFriendsForever.stream().sorted(byName()).map(Person::getFullName).collect(Collectors.joining(",")));

    long numberOfFemaleBffs = bestFriendsForever.stream().filter(friend -> friend.gender.equals("Female")).count();
    print(numberOfFemaleBffs);

    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findFirst().orElse(new Person()));

    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
    print(bestFriendsForever.parallelStream().filter(friend -> friend.gender.equals("Male")).findAny().orElse(new Person()));
  }

  private void print(Person person) {
    System.out.println(person.getFullName());
  }

  private void print(long l) {
    System.out.println(l);
  }

  private void print(String s) {
    System.out.println(s);
  }

  private Comparator<Person> byName() {
    return Comparator.comparing(Person::getSurName);
  }
}
