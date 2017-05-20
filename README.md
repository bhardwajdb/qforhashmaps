# qforhashmaps
##A reactive message broker

qforhashmaps is a message broker written in __[RX Java](http://reactivex.io/ RX Java)__ that allows multiple producers to write to it, and multiple consumers to read from it. It runs on a single server. Whenever a producer writes to qforhashmaps, a message ID is generated and returned as confirmation. Whenever a consumer polls qforhashmaps for new messages, it gets those messages which have NOT yet been processed by any other consumer that may be concurrently accessing qforhashmaps.  

For building it, use maven (run "mvn clean install javadoc:javadoc"), and deploy the resultant war file in a J2EE container with __Servlets 3.1__ support such as Tomcat 8. If deployed on containers adhering to lower versions than the Servlet 3.1 specification, the benefits of a reactive architecture with RX Java will not be completely exploited.

The message broker uses simple HTTP endpoints for its functions. There are 3 functions that it can serve:  

1. Read:  
  _http://{HOST}:{PORT}/qforhashmaps/read_  
  This GET request will either return a JSON with a payload, a UUID and a status. This method will return immediately without waiting.  
  _http://{HOST}:{PORT}/qforhashmaps/readWithBlocking_  
  This GET request will return a JSON with a payload, its UUID and a status or wait until one becomes available until timeout.   

2. Write:  
_http://{HOST}:{PORT}/qforhashmaps/write_    
This POST request will write to the queue in a FIFO fashion the payload and return a JSON with a UUID and a status. Example POST request: {"data":"Hello!"}  
Response: {"status":"OK","data":{"payload":"","uuid":"1471178519213_-5093567906476327607"}}  

3. Accept Acknowledgement:   
_http://{HOST}:{PORT}/qforhashmaps/ack/{uuid}_  
This GET request will expel the relavant packet from the system if the acknowledgement is found to be within the expiry time limit of the packet's read time, otherwise this packet will be reinitiated to the head of the queue after its expiry. This will always return a 200 OK.
