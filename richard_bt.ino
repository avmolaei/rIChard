#include <SoftwareSerial.h>

int out1 = 7;
int out2 = 8;
int out3 = 9;
int out4 = 10;
int out5 = 11;
int out6 = 12; 


String cmd1;
String cmd2;
String cmd3;
String cmd4;

String cmd = "";
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
  Serial.println("\n\n--------------------\ndémarrage...");
}

void loop() {
  while (bt.available() > 0) {
    cmd = bt.readString();
    cmd.replace(" ",";");
    cmd.replace("\n", ";");
    
    cmd1 = cmd.substring(0, 8);
    cmd2 = cmd.substring(10, 18);
    cmd3 = cmd.substring(20, 28);
    cmd4 = cmd.substring(30, 38);
    Serial.print("\nBT RX: "+cmd);
    if (cmd != "") {
     // Serial.println(cmdWord+"\n"+cmdRoom+"\n"+cmdDevice+"\n");

Serial.println("\n\nSUM(cmd): "+cmd1+cmd2+cmd3+cmd4);
    if(cmd1.equals("chamb1ON")){
      digitalWrite(out1, HIGH);
      Serial.println(cmd1+": chambre 1 allumée\n");
    }
     if(cmd1.equals("chamb1OF")){
      digitalWrite(out1, LOW);
      Serial.println(cmd1+": chambre 1 éteinte\n");
    }

    if(cmd2.equals("chamb2ON")){
      digitalWrite(out2, HIGH);
      Serial.println(cmd2+": chambre 2 allumée\n");
    }
     if(cmd2.equals("chamb2OF")){
      digitalWrite(out2, LOW);
      
      Serial.println(cmd2+": chambre 2 éteinte\n");
    }


    if(cmd3.equals("salon1ON")){
      digitalWrite(out3, HIGH);
      Serial.println(cmd3+": salon 1 allumée\n");
    }
     if(cmd3.equals("salon1OF")){
      digitalWrite(out3, LOW);
      Serial.println(cmd3+": salon 1 éteinte\n");
    }

    if(cmd4.equals("salon2ON")){
      digitalWrite(out4, HIGH);
      Serial.println(cmd4+": salon 2 allumée\n");
    }
     if(cmd4.equals("salon2OF")){
      digitalWrite(out4, LOW);
      Serial.println(cmd4+": salon 2 éteinte\n");
    }

    



      

    }
    }
    }
