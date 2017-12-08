#include <ESP8266WiFi.h>
 
const char* ssid = "NETWORK NAME HERE";
const char* password = "PASSWORD HERE";

WiFiServer server(80);
 
void setup() {
  Serial.begin(115200);
  delay(10);

  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(8, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(2, OUTPUT);

  int i;

  for(i=0;i<3;++i)
  {

  digitalWrite(13, HIGH);
  digitalWrite(12, HIGH);
  digitalWrite(11, HIGH);
  digitalWrite(10, HIGH);
  digitalWrite(9, HIGH);
  digitalWrite(8, HIGH);
  digitalWrite(4, HIGH);
  digitalWrite(3, HIGH);
  digitalWrite(2, HIGH);

  delay(1000);

  digitalWrite(13, LOW);
  digitalWrite(12, LOW);
  digitalWrite(11, LOW);
  digitalWrite(10, LOW);
  digitalWrite(9, LOW);
  digitalWrite(8, LOW);
  digitalWrite(4, LOW);
  digitalWrite(3, LOW);
  digitalWrite(2, LOW);

  delay(1000);
  }
 
  WiFi.begin(ssid, password);
 
 
  while (WiFi.status() != WL_CONNECTED) {
    digitalWrite(13, HIGH);
    delay(250);
    digitalWrite(13, LOW);
    delay(250);
  }
  server.begin();
  Serial.print(WiFi.localIP());
  for(i=0;i<5;++i)
  {
    digitalWrite(9,HIGH);
    delay(100);
    digitalWrite(9,LOW);
    delay(100);
  }

  digitalWrite(13, HIGH); // SET TO 0
}
 
void loop() {
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
  while(!client.available()){
    delay(1);
  }
 
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();
 
  // Match the request
  if (request.indexOf("/0") != -1)  {
    digitalWrite(13, LOW);
  digitalWrite(12, LOW);
  digitalWrite(11, LOW);
  digitalWrite(10, LOW);
  digitalWrite(9, LOW);
  digitalWrite(8, LOW);
  }
  if (request.indexOf("/1") != -1)  {
    digitalWrite(13, HIGH);
  digitalWrite(12, LOW);
  digitalWrite(11, LOW);
  digitalWrite(10, LOW);
  digitalWrite(9, LOW);
  digitalWrite(8, LOW);
  }
   if (request.indexOf("/2") != -1)  {
    digitalWrite(13, HIGH);
  digitalWrite(12, HIGH);
  digitalWrite(11, LOW);
  digitalWrite(10, LOW);
  digitalWrite(9, LOW);
  digitalWrite(8, LOW);
  }
   if (request.indexOf("/3") != -1)  {
    digitalWrite(13, HIGH);
  digitalWrite(12, HIGH);
  digitalWrite(11, HIGH);
  digitalWrite(10, LOW);
  digitalWrite(9, LOW);
  digitalWrite(8, LOW);
  }
   if (request.indexOf("/4") != -1)  {
    digitalWrite(13, HIGH);
  digitalWrite(12, HIGH);
  digitalWrite(11, HIGH);
  digitalWrite(10, HIGH);
  digitalWrite(9, LOW);
  digitalWrite(8, LOW);
  }
   if (request.indexOf("/5") != -1)  {
    digitalWrite(13, HIGH);
  delay(100);
  digitalWrite(12, HIGH);
  delay(100);
  digitalWrite(11, HIGH);
  delay(100);
  digitalWrite(10, HIGH);
  delay(100);
  digitalWrite(9, HIGH);
  delay(1000);

  digitalWrite(13, LOW);
  digitalWrite(12, LOW);
  digitalWrite(11, LOW);
  digitalWrite(10, LOW);
 
  delay(100);
  digitalWrite(9, HIGH);
  }
  //low-medium-high-off
  if (request.indexOf("/l") != -1)  {
    digitalWrite(4,HIGH)
    digitalWrite(3,LOW);
    digitalWrite(2,LOW);
  }
   if (request.indexOf("/m") != -1)  {
    digitalWrite(4,HIGH)
    digitalWrite(3,HIGH);
    digitalWrite(2,LOW);
  }
   if (request.indexOf("/h") != -1)  {
    digitalWrite(4,HIGH)
    digitalWrite(3,HIGH)
    digitalWrite(2,HIGH);
  }
  if (request.indexOf("/o") != -1)  {
    digitalWrite(4,LOW)
    digitalWrite(3,LOW)
    digitalWrite(2,LOW);
  }

  // Return the response
  client.println("HTTP/1.1 200 OK");
  client.println("Content-Type: text/html");
  client.println(""); //  do not forget this one
  client.println("<!DOCTYPE HTML>");
  client.println("<html>");
 client.println("Recieved. Updated frame");
  client.println("</html>");
 
}
 
