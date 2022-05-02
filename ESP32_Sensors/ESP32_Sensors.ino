#include <dht.h> // install DHT_Library from https://www.brainy-bits.com/post/how-to-use-the-dht11-temperature-and-humidity-sensor-with-an-arduino
#include "EspMQTTClient.h"
#include <WiFi.h> 
#include <WiFiClientSecure.h>
#include "esp_wpa2.h"
#include <Wire.h> 
#include "credentials.h" // Contains user/pass info that I'm not comfortable sharing

/*  NOTES AS OF 04/11/2022
 *  This code presently will connect to rpi or home wifi given credentials in the header file
 *  It will then try to establish MQTT communication with the Camp VM's ip address specified in client
 *  
 *  This code is designed to receive message from Node-RED server, the message will cover information
 *  such as the esp32 in which building, floor, room, and the type of function. The ESP32 can work as
 *  a dump data collector collect one type of information from PIR_PIN sensor, temperature/humidity sensor,
 *  and light sensor.
 */

dht DHT;
// DHT 11 humidity and temperature sensor pin
#define DHT11_PIN 26
// pir sensor pin
#define PIR_PIN 27
// Photoresistor sensor pin
#define PHOTO_PIN 33
// MQTT topic used to receive message from Node-RED server
const String receive_topic = "device2";

const char* ssid = "207C";
const char* password = "1145141919";
const char* ssidIP = "52.55.213.194";


const int VIN = 3.3; 

int counter = 0;
int PIR_count = 0;
unsigned long dataVar1 = 0;
unsigned long dataVar2 = 0;
char flag = 1;
char PIR_flag = 0;
int ESPmode = 1;
bool detect = false;
String msgMode;
String mode1 = "Room_T";
String mode2 = "Room_H";
const String tempMode = "Room_T";
const String humiMode = "Room_H";
const String lightMode = "Room_L";
const String PIRMode = "Room_M";


EspMQTTClient client(
/*
  Taken from : https://github.com/plapointe6/EspMQTTClient
  EspMQTTClient(
    const char* mqttServerIp,
    const short mqttServerPort,  // It is mandatory here to allow these constructors to be distinct from those with the Wifi handling parameters
    const char* mqttUsername,    // Omit this parameter to disable MQTT authentification
    const char* mqttPassword,    // Omit this parameter to disable MQTT authentification
    const char* mqttClientName = "ESP8266");
*/
   //MQTT ONLY
   ssid,            //WIFI SSID
   password,        //WIFI Password
   ssidIP,          // MQTT Broker server ip, THIS MIGHT CHANGE
   "Device1",         // TestClient, Client name that uniquely identify your device 
   1883               // Port number
   
);

//Trial of code from https://www.youtube.com/watch?v=bABHeMea-P0&ab_channel=DebashishSahu
static const char incommon_ca[] PROGMEM = R"EOF(
-----BEGIN CERTIFICATE-----
MIIF+TCCA+GgAwIBAgIQRyDQ+oVGGn4XoWQCkYRjdDANBgkqhkiG9w0BAQwFADCB
iDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCk5ldyBKZXJzZXkxFDASBgNVBAcTC0pl
cnNleSBDaXR5MR4wHAYDVQQKExVUaGUgVVNFUlRSVVNUIE5ldHdvcmsxLjAsBgNV
BAMTJVVTRVJUcnVzdCBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTQx
MDA2MDAwMDAwWhcNMjQxMDA1MjM1OTU5WjB2MQswCQYDVQQGEwJVUzELMAkGA1UE
CBMCTUkxEjAQBgNVBAcTCUFubiBBcmJvcjESMBAGA1UEChMJSW50ZXJuZXQyMREw
DwYDVQQLEwhJbkNvbW1vbjEfMB0GA1UEAxMWSW5Db21tb24gUlNBIFNlcnZlciBD
QTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJwb8bsvf2MYFVFRVA+e
xU5NEFj6MJsXKZDmMwysE1N8VJG06thum4ltuzM+j9INpun5uukNDBqeso7JcC7v
HgV9lestjaKpTbOc5/MZNrun8XzmCB5hJ0R6lvSoNNviQsil2zfVtefkQnI/tBPP
iwckRR6MkYNGuQmm/BijBgLsNI0yZpUn6uGX6Ns1oytW61fo8BBZ321wDGZq0GTl
qKOYMa0dYtX6kuOaQ80tNfvZnjNbRX3EhigsZhLI2w8ZMA0/6fDqSl5AB8f2IHpT
eIFken5FahZv9JNYyWL7KSd9oX8hzudPR9aKVuDjZvjs3YncJowZaDuNi+L7RyML
fzcCAwEAAaOCAW4wggFqMB8GA1UdIwQYMBaAFFN5v1qqK0rPVIDh2JvAnfKyA2bL
MB0GA1UdDgQWBBQeBaN3j2yW4luHS6a0hqxxAAznODAOBgNVHQ8BAf8EBAMCAYYw
EgYDVR0TAQH/BAgwBgEB/wIBADAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUH
AwIwGwYDVR0gBBQwEjAGBgRVHSAAMAgGBmeBDAECAjBQBgNVHR8ESTBHMEWgQ6BB
hj9odHRwOi8vY3JsLnVzZXJ0cnVzdC5jb20vVVNFUlRydXN0UlNBQ2VydGlmaWNh
dGlvbkF1dGhvcml0eS5jcmwwdgYIKwYBBQUHAQEEajBoMD8GCCsGAQUFBzAChjNo
dHRwOi8vY3J0LnVzZXJ0cnVzdC5jb20vVVNFUlRydXN0UlNBQWRkVHJ1c3RDQS5j
cnQwJQYIKwYBBQUHMAGGGWh0dHA6Ly9vY3NwLnVzZXJ0cnVzdC5jb20wDQYJKoZI
hvcNAQEMBQADggIBAC0RBjjW29dYaK+qOGcXjeIT16MUJNkGE+vrkS/fT2ctyNMU
11ZlUp5uH5gIjppIG8GLWZqjV5vbhvhZQPwZsHURKsISNrqOcooGTie3jVgU0W+0
+Wj8mN2knCVANt69F2YrA394gbGAdJ5fOrQmL2pIhDY0jqco74fzYefbZ/VS29fR
5jBxu4uj1P+5ZImem4Gbj1e4ZEzVBhmO55GFfBjRidj26h1oFBHZ7heDH1Bjzw72
hipu47Gkyfr2NEx3KoCGMLCj3Btx7ASn5Ji8FoU+hCazwOU1VX55mKPU1I2250Lo
RCASN18JyfsD5PVldJbtyrmz9gn/TKbRXTr80U2q5JhyvjhLf4lOJo/UzL5WCXED
Smyj4jWG3R7Z8TED9xNNCxGBMXnMete+3PvzdhssvbORDwBZByogQ9xL2LUZFI/i
eoQp0UM/L8zfP527vWjEzuDN5xwxMnhi+vCToh7J159o5ah29mP+aJnvujbXEnGa
nrNxHzu+AGOePV8hwrGGG7hOIcPDQwkuYwzN/xT29iLp/cqf9ZhEtkGcQcIImH3b
oJ8ifsCnSbu0GB9L06Yqh7lcyvKDTEADslIaeSEINxhO2Y1fmcYFX/Fqrrp1WnhH
OjplXuXE0OPa0utaKC25Aplgom88L2Z8mEWcyfoB7zKOfD759AN7JKZWCYwk
-----END CERTIFICATE-----
-----BEGIN CERTIFICATE-----
MIIF3jCCA8agAwIBAgIQAf1tMPyjylGoG7xkDjUDLTANBgkqhkiG9w0BAQwFADCB
iDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCk5ldyBKZXJzZXkxFDASBgNVBAcTC0pl
cnNleSBDaXR5MR4wHAYDVQQKExVUaGUgVVNFUlRSVVNUIE5ldHdvcmsxLjAsBgNV
BAMTJVVTRVJUcnVzdCBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTAw
MjAxMDAwMDAwWhcNMzgwMTE4MjM1OTU5WjCBiDELMAkGA1UEBhMCVVMxEzARBgNV
BAgTCk5ldyBKZXJzZXkxFDASBgNVBAcTC0plcnNleSBDaXR5MR4wHAYDVQQKExVU
aGUgVVNFUlRSVVNUIE5ldHdvcmsxLjAsBgNVBAMTJVVTRVJUcnVzdCBSU0EgQ2Vy
dGlmaWNhdGlvbiBBdXRob3JpdHkwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIK
AoICAQCAEmUXNg7D2wiz0KxXDXbtzSfTTK1Qg2HiqiBNCS1kCdzOiZ/MPans9s/B
3PHTsdZ7NygRK0faOca8Ohm0X6a9fZ2jY0K2dvKpOyuR+OJv0OwWIJAJPuLodMkY
tJHUYmTbf6MG8YgYapAiPLz+E/CHFHv25B+O1ORRxhFnRghRy4YUVD+8M/5+bJz/
Fp0YvVGONaanZshyZ9shZrHUm3gDwFA66Mzw3LyeTP6vBZY1H1dat//O+T23LLb2
VN3I5xI6Ta5MirdcmrS3ID3KfyI0rn47aGYBROcBTkZTmzNg95S+UzeQc0PzMsNT
79uq/nROacdrjGCT3sTHDN/hMq7MkztReJVni+49Vv4M0GkPGw/zJSZrM233bkf6
c0Plfg6lZrEpfDKEY1WJxA3Bk1QwGROs0303p+tdOmw1XNtB1xLaqUkL39iAigmT
Yo61Zs8liM2EuLE/pDkP2QKe6xJMlXzzawWpXhaDzLhn4ugTncxbgtNMs+1b/97l
c6wjOy0AvzVVdAlJ2ElYGn+SNuZRkg7zJn0cTRe8yexDJtC/QV9AqURE9JnnV4ee
UB9XVKg+/XRjL7FQZQnmWEIuQxpMtPAlR1n6BB6T1CZGSlCBst6+eLf8ZxXhyVeE
Hg9j1uliutZfVS7qXMYoCAQlObgOK6nyTJccBz8NUvXt7y+CDwIDAQABo0IwQDAd
BgNVHQ4EFgQUU3m/WqorSs9UgOHYm8Cd8rIDZsswDgYDVR0PAQH/BAQDAgEGMA8G
A1UdEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQEMBQADggIBAFzUfA3P9wF9QZllDHPF
Up/L+M+ZBn8b2kMVn54CVVeWFPFSPCeHlCjtHzoBN6J2/FNQwISbxmtOuowhT6KO
VWKR82kV2LyI48SqC/3vqOlLVSoGIG1VeCkZ7l8wXEskEVX/JJpuXior7gtNn3/3
ATiUFJVDBwn7YKnuHKsSjKCaXqeYalltiz8I+8jRRa8YFWSQEg9zKC7F4iRO/Fjs
8PRF/iKz6y+O0tlFYQXBl2+odnKPi4w2r78NBc5xjeambx9spnFixdjQg3IM8WcR
iQycE0xyNN+81XHfqnHd4blsjDwSXWXavVcStkNr/+XeTWYRUc+ZruwXtuhxkYze
Sf7dNXGiFSeUHM9h4ya7b6NnJSFd5t0dCy5oGzuCr+yDZ4XUmFF0sbmZgIn/f3gZ
XHlKYC6SQK5MNyosycdiyA5d9zZbyuAlJQG03RoHnHcAP9Dc1ew91Pq7P8yF1m9/
qS3fuQL39ZeatTXaw2ewh0qpKJ4jjv9cJ2vhsE/zB+4ALtRZh8tSQZXq9EfX7mRB
VXyNWQKV3WKdwrnuWih0hKWbt5DHDAff9Yk2dDLWKMGwsAvgnEzDHNb842m1R0aB
L6KCq9NjRHDEjf8tM7qtj3u1cIiuPhnPQCjY/MiQu12ZIvVS5ljFH4gxQ+6IHdfG
jjxDah2nGN59PRbxYvnKkKj9
-----END CERTIFICATE-----
)EOF";

//converts analog value to output voltage, to photoresistor resistance, to luminosity
int voltageToLumens(int value) { 
  float vout = float(value)*(VIN/float(4095));
  float ldrResistance =  (10000*(VIN - vout))/vout; //Calculates R2 of voltage divider, ldr = light dependent resistor
  float luminosity = 500/(ldrResistance/1000); //Converts LDR resistance to lumens
  return luminosity;
}

// Function calls sensor and returns temperature
void getTH(float & t, float & h){
    // Start the DHT11 sensor
  DHT.read11(DHT11_PIN);      // Call DHT11 to read data
  t = DHT.temperature;      // Temperature
  h = DHT.humidity;        // Humidity
}

//WiFiClientSecure client;
void setup() {
  Serial.begin(115200);
  delay(10);
  
  //Initialize all sensor pin
 
  pinMode(PIR_PIN, INPUT);       

  
  //Initialize WPA2 connection
  Serial.println();
  Serial.print("Connecting to network: ");
  Serial.println(ssid);
  WiFi.disconnect(true);  //disconnect form wifi to set new wifi connection
  WiFi.mode(WIFI_STA); //init wifi mode
  
  //find mac address (Not Needed for application)
  Serial.println("MAC Address:");
  Serial.println(WiFi.macAddress());

  WiFi.begin(ssid,password);
  Serial.println("Connecting to Home Wifi...");
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address set: ");
  Serial.println(WiFi.localIP()); //print LAN IP
  delay(10);
  
  dataVar1 = millis();
  dataVar2 = dataVar1;

}

void onTestMessageReceived(const String& message){
  flag = 0;
  delay(10);
  dataVar1 = 0;
  dataVar2 = 0;
  Serial.println("Message received is: " + message);

  int length  = message.length();
  msgMode = message.substring(length-1, length);
  mode1 = message.substring(0,length-1);
  Serial.println(mode1);
  if (msgMode == "T"){
      ESPmode = 1;
      mode1 = mode1+"T";
      mode2 = message.substring(0,length-1)+"H";
      Serial.println("MQTT topic1: "+mode1);
      Serial.println("MQTT topic2: "+mode2);
      delay(10);
      flag = 1;
  }else if (msgMode == "P"){
      ESPmode = 2;
      mode1 = mode1+ "P";
      Serial.println("MQTT topic1: "+mode1);
      delay(10);
      flag = 1;
  }else if (msgMode == "O"){
      ESPmode = 3;
      PIR_count = 0;
      PIR_flag = 0;
      mode1 = mode1+ "O";
      Serial.println("MQTT topic1: "+mode1);
      dataVar1 = millis();
      dataVar2 = dataVar1;
      delay(10);
      flag = 1;
    }
}

void onConnectionEstablished() {
  //Print statement to determine whether MQTT connection is established
  //how to subscribe + what commands to follow after receiving
  client.subscribe(receive_topic, onTestMessageReceived);
  //Topic to be changed according to room/sensor type
  client.publish(receive_topic, "Start working");
}

void loop() {
  // put your main code here, to run repeatedly:
  client.loop();
  
  if(true){
      if (ESPmode == 2){
        
        delay(1000);
      }else if(ESPmode == 1){
        float temp, humi;

        detect = false;
        dataVar1 = millis();
        
        getTH(temp, humi);
        String T = String(temp);
        String H = String(humi);
        
        dataVar1 = abs(analogRead(PHOTO_PIN));
        //Serial.println(dataVar1);
        dataVar2 = voltageToLumens(dataVar1);
        char lightString[8];
        dtostrf(dataVar2,1,2,lightString);

         if(digitalRead(PIR_PIN) == HIGH){
          detect = true;
          Serial.println("Detect Motion!");
        }

        if(detect){
          client.publish(PIRMode,"1");
          }
        client.publish(lightMode, lightString);
        
        client.publish(tempMode, T);
        client.publish(humiMode, H);

        delay(5000);
        }else if(ESPmode == 3){
        
        if (PIR_flag == 1){
            if((dataVar1-dataVar2)< 20000){return;}
            client.publish(mode1,"ESP32 wake up, start detecting motion");
            PIR_flag = 0;
            PIR_count = 0;
            dataVar2 = dataVar1;
            return;
        }
        if(digitalRead(PIR_PIN) == HIGH){
          detect = true;
        }
        if (detect && dataVar1 - dataVar2 > 5000){
            Serial.println("Over time! Counter clear");
            PIR_count = 0;
            dataVar2 = dataVar1;
        }else if (detect){
            PIR_count += 1;
            Serial.println("Detect motion!");
            if (PIR_count > 4){
                
                client.publish(mode1,"Room Occupied, sleep for 3 minutes");
                PIR_flag = 1;
            }
            dataVar2 = dataVar1;
        }
        
        delay(500);
      }

  }else{
    Serial.println("Waiting for Node-RED order to set running mode");
    delay(1000);
  }
  
  client.enableDebuggingMessages(true);
}
