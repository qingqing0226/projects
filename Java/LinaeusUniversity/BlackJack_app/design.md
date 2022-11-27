#### _Elias Hanna (eh223yw) & Qingqing Dai (qd222ab)_
# BlackJack OO-Design
This document describes the current design. Note that some dependencies have been left out for readability reasons. For example there are a lot of dependencies to the Card class.

## Class Diagram
The application uses the model-view-controller (MVC) architectural pattern. The view is passive and gets called from the controller.<br /> 
Here is the class diagram:<br /> 
Note that some dependency lines have been omitted for clarity as described in the comments.

![class diagram](img/class-diagram.png)

## Stand - Sequence Diagram
This is the detailed sequence diagram for the `Game.stand` method. This is what should be implemented.

![Stand Sequence diagram](img/stand_seq.jpg)
