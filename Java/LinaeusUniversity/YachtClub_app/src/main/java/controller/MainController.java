package controller;

import java.time.LocalDate;
import model.Boat;
import model.Member;
import model.YachtClub;
import view.Console;

public class MainController {
  private Console console = new Console();
  private YachtClub yachtClub = new YachtClub();

  /*the main menu of the app.
   either branch into other menus based on input or close the entire program.*/
  public void appStarter() {
    console.showMainMenu();
    if (console.displayMemberMenu()) {
      memberMenuUi();
    } else if (console.displayBoatMenu()) {
      boatMenuUi();
    } else if (console.exitProgram()) {
      console.goodbyeMessage();
      System.exit(0);
    } else {
      console.showWrongNumber();
      appStarter();
    }
  }

  /*the member menu with options to see a verbose list, compact list, create a member,
  delete a member, view member info, edit member info, or return to the main menu.*/
  private void memberMenuUi() {
    console.showMemberMenu();
    if (console.displayVerboseList()) {
      verboseList();
    } else if (console.displayCompactList()) {
      compactList();
    } else if (console.wantsCreateMember()) {
      createMember();
    } else if (console.wantsDeleteMember()) {
      deleteMember();
    } else if (console.wantsToEditMemberInfo()) {
      changeMemberInfo();
    } else if (console.wantsToViewMemberInfo()) {
      viewMemberInfo();
    } else if (console.wantsToReturnToMainMenu()) {
      returnToMainMenu();
    } else {
      console.showTypeError();
      memberMenuUi();
    }
  }

  /*Displays a verbose list containing the members and information regarding them.*/
  private void verboseList() {
    console.verboseList(yachtClub.getMembers());
    memberMenuUi();
  }

  /*Displays a compact list containing the members.
  and less  information regarding them compared to the verbose list.*/
  private void compactList() {
    console.compactList(yachtClub.getMembers());
    memberMenuUi();
  }

  /*Creates a member and adds them to the list of members,*/
  private void createMember() {
    yachtClub.createMember(
        console.getFirstName(), console.getLastName(), console.getPersonalNumber());
    console.memberCreated();
    memberMenuUi();
  }

  /*deletes a member from the list of members.*/
  private void deleteMember() {
    console.showMembers(yachtClub.getMembers());
    int memberIndex = console.getIndexOfMember();
    if (memberIndex >= 0 && memberIndex <= yachtClub.getMembers().size() - 1) {
      Member member = yachtClub.getMembers().get(memberIndex);
      yachtClub.deleteMember(member);
      console.showMemberDeleted();
      memberMenuUi();
    } else {
      console.showWrongNumber();
      deleteMember();
    }
  }

  /*Edit a member's information and add it to the list of members.*/
  private void changeMemberInfo() {
    console.showMembers(yachtClub.getMembers());
    int memberIndex = console.getIndexOfMember();
    if (memberIndex >= 0 && memberIndex <= yachtClub.getMembers().size() - 1) {
      Member m = null;
      m = yachtClub.getMembers().get(memberIndex);
      yachtClub.deleteMember(m);
      m.setFirstName(console.getFirstName());
      m.setLastName(console.getLastName());
      m.setPersonalNo(console.getPersonalNumber());
      yachtClub.addMember(m);
      console.editMemberSuccess();
      memberMenuUi();
    } else {
      console.showWrongNumber();
      changeMemberInfo();
    }
  }

  /*Display a specific member's information.*/
  private void viewMemberInfo() {
    console.showMembersByNames(yachtClub.getMembers());
    
    int memberIndex = console.getIndexOfMember();
    if (memberIndex >= 0 && memberIndex <= yachtClub.getMembers().size() - 1) {
      Member m = null;
      m = yachtClub.getMembers().get(console.getIndexOfMember());
      console.showSpecificMember(m);
      memberMenuUi();
    } else {
      console.showWrongNumber();
      viewMemberInfo();
    }
  }

  /*Return to the main menu in appStarter().*/
  private void returnToMainMenu() {
    try {
      appStarter();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*the boat menu with options to register a boat with specific type and size to a certain member,
  delete a boat that's assigned to a specific member,
  edit a boat's info, or return to the main menu.*/
  private void boatMenuUi() {
    console.showBoatMenu();
    console.takeUserInputForOption();
    if (console.wantsToRegisterBoat()) {
      registerBoat();
    } else if (console.wantsToDeleteBoat()) {
      deleteBoat();
    } else if (console.wantsToEditBoat()) {
      changeBoatInfo();
    } else if (console.boatToMainMenu()) {
      returnToMainMenu();
    } else {
      console.showWrongNumber();
      boatMenuUi();
    }
  }

  /*Register a boat with specific type and size to a certain member.*/
  private void registerBoat() {
    console.showMembers(yachtClub.getMembers());
    int indexer = console.getIndexOfMember();
    if (indexer >= 0 && indexer <= yachtClub.getMembers().size() - 1) {
      console.memberSelected();
      console.showSpecificMember(yachtClub.getMembers().get(indexer));
      try {
        LocalDate date = LocalDate.now();
        Boat b = new Boat(date, console.getBoatType(), console.getBoatLength());
        yachtClub.registerBoat(b, yachtClub.getMembers().get(indexer));
        console.boatRegistered();
        boatMenuUi();
      } catch (NumberFormatException e) {
        console.provideFloat();
        boatMenuUi();
      }
    } else {
      console.clearScreen();
      console.showTypeError();
      registerBoat();
    }
  }

  /*Selects a member that owns the boat that is going to be deleted.
   (continued in askForBoatIndex(Member member).*/
  private void deleteBoat() {
    console.showMembers(yachtClub.getMembers());
    int indexer = console.getIndexOfMember();
    if (indexer >= 0 && indexer <= yachtClub.getMembers().size() - 1) {
      askForBoatIndex(yachtClub.getMembers().get(indexer));
    } else {
      console.clearScreen();
      console.showWrongNumber();
      deleteBoat();
    }
  }

  /*Delete a specific boat based on input that belonged to a user provided by deleteBoat().*/
  private void askForBoatIndex(Member member) {
    console.boatsOwnedByMember(member);
    console.askForBoatToRemove();
    int boatIndex = console.getIndexOfMember();
    if (boatIndex >= 0 && boatIndex <= member.getBoats().size() - 1) {
      member.getBoats().remove(boatIndex);
      console.boatDeleted();
      boatMenuUi();
    } else {
      console.clearScreen();
      console.incorrectBoatIndex();
      askForBoatIndex(member);
    }
  }

  /*Selects a member that owns the boat that is going to be edited. 
  (continued in askForBoatIndex(Member member).*/
  private void changeBoatInfo() {
    console.showMembers(yachtClub.getMembers());
    int memberIndex = console.getIndexOfMember();
    if (memberIndex >= 0 && memberIndex <= yachtClub.getMembers().size() - 1) {
      askForBoatToChange(yachtClub.getMembers().get(memberIndex));
    } else {
      console.clearScreen();
      console.showWrongNumber();
      changeBoatInfo();
    }
  }

  /*Edits a specific boat based on input that belonged to a user provided by deleteBoat().*/
  private void askForBoatToChange(Member member) {
    console.boatsOwnedByMember(member);
    console.changeBoatMesssage();
    int boatIndex = console.getIndexOfMember();
    if (boatIndex >= 0 && boatIndex <= member.getBoats().size() - 1) {
      try {
        member.getBoats().get(boatIndex).setType(console.getBoatType());
        member.getBoats().get(boatIndex).setLength(console.getBoatLength());
        console.boatInfoChanged();
        boatMenuUi();
      } catch (NumberFormatException e) {
        console.clearScreen();
        console.provideFloat();
        askForBoatToChange(member);
      }

    } else {
      console.clearScreen();
      console.incorrectBoatIndex();
      askForBoatToChange(member);
    }
  }
}
