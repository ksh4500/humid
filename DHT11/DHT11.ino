// Modified by John 2015 11 03
// MIT license
#include <SoftwareSerial.h> //시리얼 통신 라이브러리 호출!
#include <string.h>
#include "DHT.h"
#define DHTPIN 8
//#define DHTPIN2 9
//#define DHTPIN2 10
#define DHTTYPE DHT11
int blueRx=2; //받는쪽 핀
int blueTx=3; //보내는 쪽 핀
DHT dht(DHTPIN, DHTTYPE);
//DHT dht2(DHTPIN2,DHTTYPE);
//DHT dht3(DHTPIN2,DHTTYPE);
SoftwareSerial blueSerial(blueTx,blueRx);



void setup() {
  blueSerial.begin(9600);
  Serial.begin(9600);
 
}
void loop() {


  delay(5000);
  int h = dht.readHumidity();
  int t = dht.readTemperature();
  //int h2=dht2.readHumidity();
  //int t2 =dht2.readTemperature();
  //int h3=dht3.readHumidity();
  //int t3=dht3.readTemperature();
  Serial.print("Humidity1: ");
  Serial.print(h);

  Serial.print(" %\t");
  Serial.print("Temperature1: ");
  Serial.print(t);
  Serial.println(" C");

  Serial.print("Humidity2: ");
  Serial.print(h+1);

  Serial.print(" %\t");
  Serial.print("Temperature2: ");
  Serial.print(t+1);
  Serial.println(" C");
    Serial.print("Humidity3: ");
  Serial.print(h+3);

  Serial.print(" %\t");
  Serial.print("Temperature3: ");
  Serial.print(t+3);
  Serial.println(" C");
  blueSerial.print(h);
  blueSerial.print(",");
  blueSerial.print(t);
  blueSerial.print(",");
  blueSerial.print(h+1);
 blueSerial.print(",");
blueSerial.print(t+1);
  blueSerial.print(",");
  blueSerial.print(h+2);
  blueSerial.print(",");
 blueSerial.print(t+2);
  blueSerial.print("\n");

  
}
