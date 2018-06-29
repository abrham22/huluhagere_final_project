Huluhagere is an android app that suggests a tour road map 
based on preferences collected from a user. The features 
supported by the app are described in the SRS and SDS documents
sent earlier. 

The app is tested in line with the functional and non functional
requirements specified in the project documentations.

The client side tests are located inside the 'andriodTest' folder.
These tests are written using the android espresso testing framework.
Client side functionality is tested by emulating the usecases with
the espresso testing utilities. 
Integration testing is done by testingintents that link the various activities.

Server side unit and integrationt tests are located inside the
"tests" folder. These tests were carried out using the Mocha and
Chai javascript testing frameworks.

We did not upload the 'node-modules' folder on the server side
and the generated folders on the client side. Therefore, for the
project to be run, it would need to be synced (using android
studio on the client side and using npm on the server side) first. 
Mongodb database is also required for the server to work as expected.

Group Members
-----------------
Mezigebu Zework ... ATR/0337/08
Natnael Getachew ... ATR/9147/08
Dagnachew Tesfaye  .... ATR/4262/08
Leykun Yilma .... ATR/4380/08
Leoul Mekonnen  .... ATR/8905/08
Abraham Aman  .... ATR/4750/08
Nabil Seid .... ATR/5725/08