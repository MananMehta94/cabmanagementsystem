# cabmanagementsystem

1. Register cabs.  **DONE**

2. Onboard various cities where cab services are provided. **DONE**

3. Change current city (location) of any cab. **DONE**

4. Change state of any cab. For this you will have to define a state machine for the cab ex:
a cab must have at least these two basic states; IDLE and ON_TRIP **DONE**

5. Book cabs based on their availability at a certain location. In case more than one cab are
available , use the following strategy;

a. Find out which cab has remained idle the most and assign it. **DONE**

b. In case of clash above, randomly assign any cab **DONE**

Assumption : a cab once assigned a trip cannot cancel/reject it

**BONUS**

a. Provide insights such as for how much time was a cab idle in a given duration ? **DONE**

b. Keep a record of the cab history of each cab. (A cab history could just be a record of
what all states a cab has gone through) **DONE**
