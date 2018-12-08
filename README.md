# Indoor-Localisation 

### RSS based Dynamic Circle Expansion
> This is the WiFi module of a bigger state funded project Major part of the code is in a private repository. This is the Little code that I can put out! 

##### The Experiment 

Data Collection : Me and the research fellow I was working with developed an android applications to continously records the WiFi strengths for 20 seconds and stores the estimated average strenghts. The whole setup of the Belkin WiFi routers and sampling signal strenghts at 100s of different points in the environment took several hours and these experiments were repeated a number of times with different settings in the late hours of the day when the university buildings were empty. 

[Data](https://github.com/bsridatta/Indoor-Localisation/blob/master/TheWifiExperiment/Resources/Data.csv)
This is a sample of the WiFi readings taken to test the circle expansion algorithm.

[Config.properties](https://github.com/bsridatta/Indoor-Localisation/blob/master/TheWifiExperiment/Resources/config.properties)
This is the ground truth of the WiFi routers in the indoor environment. The setup was carefully set and reading were taken with the best possible precision.

[Results](https://github.com/bsridatta/Indoor-Localisation/blob/master/TheWifiExperiment/Resources/results.csv)
The results using this naive algorithm in this repo were as not as good as expected and we have improvised the results using different multipoint trilatration techniques. The biggest problem with the estimation was that the environment was too complex to have a generealised alogrithm. The university buildings had thick brick walls and many windows making the penetration of the long range 2.5GHz hard and the reflections of the signals are suspected to cause inconsistencies in the strengths from place to place. For this problem, our proposed solution was to learn the enivornmental parameters of the trilatertion algorithm and to adaptively tune it to based on the percieved router location. Thus achieving an adaptive localisation algorithm. This work is being continued by my fellow researchers at [AMUDA labs](https://www.amrita.edu/school/engineering/coimbatore/computer-science/resources/amuda-lab).   

We then built another android app to localise the users on a scaled map of the building.

[DCE]( the 

```
Indoor-Localisation

└── TheWifiExperiment
    ├── lib
    │   └── opencsv-3.9.jar
    ├── Resources
    │   ├── config.properties
    │   ├── Data.csv
    │   └── results.csv
    └── src
        └── trilateration
            ├── Circle.java
            ├── CSV_handler.java
            ├── CSV_writer.java
            ├── DCE.java
            ├── Location.java
            ├── QueueArray.java
            ├── QueueIntf.java
            └── TigerShark.java
```
