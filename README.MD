TODOs
- enable activemq communication + start dockerized activemq server
- add integration tests using testcontainers using test profile
https://java.testcontainers.org/modules/activemq/
- add moreunit tests
- use lombok to eliminate boiler code for setters and getters

HOW TO TEST:
use Packet Sender app
send packet to 127.0.0.1:3344 with text: sensor_id=h1;value=40