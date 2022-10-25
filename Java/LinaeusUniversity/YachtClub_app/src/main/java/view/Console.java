package view;

import java.util.Scanner;
import model.Boat;
import model.Boat.Type;
import model.Member;

public class Console {
  Scanner scanner = new Scanner(System.in);
  private int userinput;
  private String inputStr = "";

  /*The start of main menu*/

  /*
   * Displays the main menu and takes user input.
   */
  public void showMainMenu() {
    System.out.println("**************************************");
    System.out.println("Welcome to Yacht Club!");
    System.out.println("[1] Member Related Options");
    System.out.println("[2] Boat Related Options");
    System.out.println("[3] Exit");
    System.out.print("Enter a number: ");
    takeUserInputForOption();
  }

  /*checks whether the user inputs corresponds to them wanting to go to the member menu.*/
  public boolean displayMemberMenu() {

    return userinput == 1;
  }

  /*checks whether the user inputs corresponds to them wanting to go to the boat menu.*/
  public boolean displayBoatMenu() {

    return userinput == 2;
  }

  /*checks whether the user inputs corresponds to them wanting to exit the program.*/
  public boolean exitProgram() {

    return userinput == 3;
  }

  /*Displays a message that lets the user know they chose the wrong type of input. 
    i.e, string rather than int etc., .*/
  public void showTypeError() {
    System.out.println("Please write an INTEGER that corresponds to the option you want to choose");
  }

  /*Displays a message that lets the user know they chose the wrong number. 
    i.e, 5 in a list counting 4 options etc.*/
  public void showWrongNumber() {
    System.out.println("\nWrong input!\nPlease write one of the specified integers.\n");
  }

  /*Displays a specific member and their corresponding information .*/
  public void showSpecificMember(Member member) {
    System.out.println("\n<<<<Member Information>>>>");
    System.out.println("Name: " + member.getFirstName() + " " + member.getLastName());
    System.out.println("Personal Number: " + member.getPersonalNo());
    System.out.println("MemberID: " + member.getMemberId());
    System.out.println("Boats: ");
    for (Boat b : member.getBoats()) {
      System.out.println(
          ""
              + b.getType()
              + " "
              + b.getLength()
              + "m "
              + " registered on "
              + b.getRegistrationDate().toString());
    }
    System.out.println();
  }

  /*Displays a message that lets the user know they chose the wrong type of input i.e, int rather than string etc., .*/
  public void showValueError() {
    System.out.println("Please Provide name in string and personal number in integer values!");
  }

  /*Displays a message that lets the user know the program is being terminated .*/
  public void goodbyeMessage() {
    System.out.println("\nGoodbye!");
  }

  /*Displays a message that lets the user know they are supposed to enter the member id.*/
  public void enterMemberId() {
    System.out.println("Enter member id: ");
  }

  /*Displays a message that lets the user know that.
    the member that they selected exists in the database.*/
  public void memberSelected() {
    System.out.println("Member has been found!\nHere is the information: ");
  }

  /*Displays a message that lets the user know the member that. 
    they selected does not exist in the database.*/
  public void memberIdIncorrect() {
    System.out.println("Incorrect member id!\nPlease try again.");
  }

  /*Displays a message that lets the user know they should provide a float.*/
  public void provideFloat() {
    System.out.println(
        "Please write an NUMBER not a string for boat length , you will be returned to the menu");
  }

  /*The end of main menu*/

  /*The start of Member Menu.*/

  /*Shows the possible options to choose from in the member menu, and takes input.*/
  public void showMemberMenu() {
    System.out.println("**************************************");
    System.out.println("Members");
    System.out.println("[1] Member Verbose List");
    System.out.println("[2] Member Compact List");
    System.out.println("[3] Create A Member");
    System.out.println("[4] Delete A Member");
    System.out.println("[5] Change Member Information");
    System.out.println("[6] View Member Information");
    System.out.println("[7] Return to Main Menu");
    System.out.println("Enter a number: ");
    takeUserInputForOption();
  }

  /*Shows the list of members from an iterable object.*/
  public void showMembers(Iterable<Member> members) {
    System.out.println("index \t firstname \t lastname \t Personal number \tId");
    int count = 0;
    for (model.Member member : members) {
      System.out.printf(
          "%-9d%-16s%-16s%-23s%s",
          count,
          member.getFirstName(),
          member.getLastName(),
          member.getPersonalNo(),
          member.getMemberId());
      System.out.println();
      count++;
    }
    System.out.println("Write the index of the member: ");
  }

  /*Shows the verbose list of members from an iterable object.*/
  public void verboseList(Iterable<Member> members) {
    System.out.println("\nFirstName  LastName  PersonalNo  MemberID  Boats & Registration Date");
    for (Member member : members) {
      System.out.printf(
          "%-11s%-10s%-10s  %-8s",
          member.getFirstName(),
          member.getLastName(),
          member.getPersonalNo(),
          member.getMemberId());
      for (Boat boat : member.getBoats()) {
        System.out.print(
            "  "
                + boat.getType()
                + " "
                + boat.getLength()
                + "m "
                + boat.getRegistrationDate()
                + " ");
      }
      System.out.println();
    }
  }

  /*Shows the compact list of members from an iterable object.*/
  public void compactList(Iterable<Member> members) {
    System.out.println("\nFirstName LastName  MemberID\tNumber of Boats ");
    for (Member member : members) {
      System.out.printf(
          "%-10s%-10s%-10s  %d",
          member.getFirstName(),
          member.getLastName(),
          member.getMemberId(),
          member.getBoats().size());
      System.out.println();
    }
  }

  /*Displays a message that lets the user know they should provide a string first name.*/
  public void enterFirstName() {

    System.out.println("Enter first name: ");
  }

  /*Displays a message that lets the user know they should provide a string last name.*/
  public void enterLastName() {

    System.out.println("Enter last name: ");
  }

  /*Displays a message that lets the user know they should provide an int for Personal Number.*/
  public void enterPersonalNo() {

    System.out.println("Enter personal number: ");
  }

  /*Displays a message that lets the user know the member is created.*/
  public void memberCreated() {

    System.out.println("The member has been created.\n");
  }

  /*Displays a message that lets the user know the member is deleted.*/
  public void showMemberDeleted() {
    System.out.println("The member has been deleted!");
  }

  /*Displays a message that lets the user know the editing of the member's information has been successful.*/
  public void editMemberSuccess() {

    System.out.println("Member information has been updated successfully.");
  }

  /*Displays a message that shows the members by name from an iterable object.*/
  public void showMembersByNames(Iterable<Member> members) {
    int count = 0;
    for (Member member : members) {
      System.out.println("[" + count + "] " + member.getFirstName() + " " + member.getLastName());
      count++;
    }
    System.out.print("Enter the index of a member: ");
  }

  /*takes a user input in the form of an int.*/
  public void takeUserInputForOption() {
    userinput = -1;
    if (scanner.hasNextInt()) {
      userinput = scanner.nextInt();
    } else {
      showTypeError();
      scanner.nextLine();
    }
  }

  /*checks whether the user inputs corresponds to them wanting to see a verbose list.*/
  public boolean displayVerboseList() {
    return userinput == 1;
  }

  /*checks whether the user inputs corresponds to them wanting to see a compact list.*/
  public boolean displayCompactList() {
    return userinput == 2;
  }

  /*checks whether the user inputs corresponds to them wanting to create a member.*/
  public boolean wantsCreateMember() {
    return userinput == 3;
  }

  /*checks whether the user inputs corresponds to them wanting to delete a member.*/
  public boolean wantsDeleteMember() {
    return userinput == 4;
  }

  /*checks whether the user inputs corresponds to them wanting to edit a member's information.*/
  public boolean wantsToEditMemberInfo() {
    return userinput == 5;
  }

  /*checks whether the user inputs corresponds to them wanting to view a member's information.*/
  public boolean wantsToViewMemberInfo() {
    return userinput == 6;
  }

  /*checks whether the user inputs corresponds to them wanting to go to the main menu.*/
  public boolean wantsToReturnToMainMenu() {
    return userinput == 7;
  }

  /*Takes a user input in the form of a string.*/
  public void takeUserInputString() {
    if (scanner.hasNextLine()) {
      inputStr = scanner.next();
    }
  }

  /*Adds a first name from input and returns it*/
  public String getFirstName() {
    enterFirstName();
    scanner.nextLine();
    takeUserInputString();
    return inputStr;
  }

  /*Adds a last name from input and returns it*/
  public String getLastName() {
    enterLastName();
    takeUserInputString();
    return inputStr;
  }

  /*Adds a personal number from input and returns it*/
  public String getPersonalNumber() {
    enterPersonalNo();
    takeUserInputString();
    return inputStr;
  }

  /*Takes an input in the form of an int and returns that number.*/
  public int getIndexOfMember() {
    takeUserInputForOption();
    return userinput;
  }

  /*The end of Member Menu.*/

  /*Shows all the possible options to choose from in the boat menu.*/
  public void showBoatMenu() {
    System.out.println("**************************************");
    System.out.println("Boats");
    System.out.println("[1] Register Boat");
    System.out.println("[2] Delete Boat");
    System.out.println("[3] Change Boat Information");
    System.out.println("[4] Return to Main Menu");
    System.out.print("Enter a number: ");
  }

  /*checks whether the user inputs corresponds to them wanting to register a boat to a member.*/
  public boolean wantsToRegisterBoat() {
    return userinput == 1;
  }

  /*checks whether the user inputs corresponds to them wanting to delete a boat.*/
  public boolean wantsToDeleteBoat() {
    return userinput == 2;
  }

  /*checks whether the user inputs corresponds to them wanting to edit a boat's information.*/
  public boolean wantsToEditBoat() {
    return userinput == 3;
  }

  /*checks whether the user inputs corresponds to them wanting to return to the main menu.*/
  public boolean boatToMainMenu() {
    return userinput == 4;
  }

  /*Takes a user input in the form of a string.*/
  public Type getBoatType() {
    enterBoatType();
    takeUserInputString();
    Boat.Type t = Boat.Type.OTHER;
    if (inputStr.equalsIgnoreCase("Sailboat")) {
      t = Boat.Type.SAILBOAT;
    }
    if (inputStr.equalsIgnoreCase("motorsailer")) {
      t = Boat.Type.MOTORSAILER;
    }
    if (inputStr.equalsIgnoreCase("canoe")) {
      t = Boat.Type.CANOE;
    }
    return t;
  }

  /*Takes a user input in the form of a double.*/
  public double getBoatLength() {
    scanner.nextLine();
    enterBoatLength();
    try {
      return Double.parseDouble(scanner.nextLine());
    } catch (NumberFormatException e) {
      provideFloat();
      getBoatLength();
    }
    return -1;
  }
  /*Lets the user know they should enter a boat type.*/
  public void enterBoatType() {
    System.out.println("\nEnter boat type: ");
  }

  /*Lets the user know they should enter a boat length.*/
  public void enterBoatLength() {
    System.out.println("Enter boat length: ");
  }

  /*Lets the user know a boat has been registered.*/
  public void boatRegistered() {
    System.out.println("Boat has been registered!");
  }

  /*Displays all the boats owned by a member.*/
  public void boatsOwnedByMember(Member member) {
    System.out.println("Name: " + member.getFirstName() + " " + member.getLastName());
    for (Boat b : member.getBoats()) {
      System.out.println(
          "["
              + member.getBoats().indexOf(b)
              + "] "
              + b.getType()
              + " "
              + b.getLength()
              + "m registered on "
              + b.getRegistrationDate());
    }
  }

  /*Lets the user know they should write a number.*/
  public void changeBoatMesssage() {
    System.out.println("Enter boat index: ");
  }

  /*Lets the user know they should write a number.*/
  public void askForBoatToRemove() {
    System.out.println("Enter boat index to remove the boat: ");
  }

  /*Lets the user know a boat has been removed successfully .*/
  public void boatDeleted() {
    System.out.println("The boat has been removed!");
  }

  /*Lets the user know they index they provided was incorrect .*/
  public void incorrectBoatIndex() {
    System.out.println("Incorrect boat index!\nPlease try again.");
  }

  /*Lets the user know the boat information have been changed.*/
  public void boatInfoChanged() {
    System.out.println("Boat information has been changed!");
  }

  /*Clears the screen in the terminal .*/
  public void clearScreen() {
    for (int i = 0; i < 50; i++) {
      System.out.println();
    }
  }
}
