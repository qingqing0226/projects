#  YachtClub
### _**Author**:_ Qingqing Dai
#### _**IDE**:_ Visual Studio Code
#### _**language**:_ Java

## **3 main parts**
The three main parts are Controller, Model, View.
Everything within the code falls under these 3 packages. I will explain every package and what classes I insert within it:
- ### **Controller**
Within the controller there are 2 classes : 
1. _`App`_: this class has the main method (**Run this file**).
2. _`MainController`_: This class is the class where all the functionality happens, it is a generic controller class.
- ### **Model**
within model, there are 5 classes:
1. _`Member`_: class contains basic information about a member (Last name, First name, Personal Number, Member ID, BoatList)
2. _`Boat`_: class contains basic information about a boat( Length, Type registration date)
3. _`YachtClub`_: It contains most of the functions for member and boat, and we generate a member id here, we do not use the functions here,
4. _`Persistence`_: an interface that is implemented by Data
5. _`Data`_: contains hardcoded information regarding members and their boats.
- ### **View**
- within model, I have one class that does all what is required of view, displaying messages & getting user input:
1. _`Console`_: this is single class of view, it takes user input and displays messages and information that is required to be shown.
