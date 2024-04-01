# JHySim
 Java Hydrodynamics Simulations
 
## What is it? 
 Here is a software to perform some easily hydrodynamics simulations in Java
 
## Plug a new simulation
* reach for the xml file path name
* check if the .class file really exists
* if this is the case, put the new simulation in the "plugged_simulations.xml" XML file

* update the simulations.xml file
* create a .class file of your simulation class
* create a your_simulation.xml file where are listed the profiles and variables which are needed to initialize your new simulation (the Spatial Resolution double variable has to be the first of the double variable in the XML file)