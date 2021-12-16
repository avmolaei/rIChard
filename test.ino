#include <SoftwareSerial.h>

int out1 = 7;
int out2 = 8;
int out3 = 9;
int out4 = 10;
int out5 = 11;
int out6 = 12; 

String cmd = "";
String cmdWord;
String cmdRoom;
String cmdDevice;
SoftwareSerial bt(2, 3);
void setup() {
  bt.begin(9600);
  Serial.begin(9600);
  pinMode(out1, OUTPUT);
  pinMode(out2, OUTPUT);
  pinMode(out3, OUTPUT);
  pinMode(out4, OUTPUT);
  pinMode(out5, OUTPUT);
  pinMode(out6, OUTPUT);
  Serial.println("démarrage...");
}

void loop() {
  while (bt.available() > 0) {
    cmd = bt.readString();
    Serial.println("Recu: " + cmd + "\n");
    if (cmd != "") {
      Serial.println("Recu: " + cmd + "\n");
      cmdWord = cmd.substring(0, 2);
      cmdRoom = cmd.substring(3, 8);
      cmdDevice = cmd.substring(9);
      Serial.println(cmdWord+"\n"+cmdRoom+"\n"+cmdDevice+"\n");

      if(cmdRoom.equals("salon")){
        if(cmdDevice.equals("1")){//lampe
          if(cmdWord.equals("on")){
            digitalWrite(out1, HIGH);
            Serial.println("on, salon, 1");
          }
          else if(cmdWord.equals("of")){
            digitalWrite(out1   , LOW);
          }
        }
        if(cmdDevice.equals("2")){//ordi
          if(cmdWord.equals("on")){
            digitalWrite(out4, HIGH);
            Serial.println("on, salon, 2");
          }
          else if(cmdWord.equals("of")){
            digitalWrite(out4, LOW);
          }
        }
        if(cmdDevice.equals("3")){//pc
          if(cmdWord.equals("on")){
            digitalWrite(out3, HIGH);
            Serial.println("on, salon, 3");
          }
          else if(cmdWord.equals("of")){
            digitalWrite(out3, LOW);
          }
        }
      }
      if(cmdRoom.equals("chamb")){
        if(cmdDevice.equals("1")){//lampe
          if(cmdWord.equals("on")){
            digitalWrite(out5, HIGH);
            Serial.println("on, chamb, 1");
          }
          else if(cmdWord.equals("of")){
            digitalWrite(out5, LOW);
          }
        }
        if(cmdDevice.equals("2")){//ordi
          if(cmdWord.equals("on")){
            digitalWrite(out2, HIGH);
            Serial.println("on, chamb, 2");
          }  
          else if(cmdWord.equals("of")){
            digitalWrite(out2, LOW);
          } 
        }
        if(cmdDevice.equals("3")){//pc
          if(cmdWord.equals("on")){
            digitalWrite(out6, HIGH);
            Serial.println("on, chamb, 3");
          }
          else if(cmdWord.equals("of")){
            digitalWrite(out6, LOW);
          }
        }
      }

      
    }
  }  
}
