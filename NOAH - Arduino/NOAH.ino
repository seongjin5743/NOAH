#include <Arduino.h>
#include <Servo.h>
#if defined(ESP32) || defined(ARDUINO_RASPBERRY_PI_PICO_W)
#include <WiFi.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#endif

#include <Firebase_ESP_Client.h>

// Provide the token generation process info.
#include <addons/TokenHelper.h>

// Provide the RTDB payload printing info and other helper functions.
#include <addons/RTDBHelper.h>

/* 1. Define the WiFi credentials */
#define WIFI_SSID "iiiPhone"
#define WIFI_PASSWORD "1234567890"

// For the following credentials, see examples/Authentications/SignInAsUser/EmailPassword/EmailPassword.ino

/* 2. Define the API Key */
#define API_KEY "AIzaSyBVKpa8AiJmmnQFTQU9XWhFECMUsUaYGk8"

/* 3. Define the RTDB URL */
#define DATABASE_URL "noah-7bfd8-default-rtdb.firebaseio.com/" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

/* 4. Define the user Email and password that alreadey registerd or added in your project */
#define USER_EMAIL "sangwon1786@gmail.com"
#define USER_PASSWORD "123456"

// Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;

//모터
int motor1 = D2;
int motor2 = D3;

// 서보 모터 객체 생성
Servo servo1;
Servo servo2;

// 초음파 센서 핀 설정
int trig = D9; // Trig pin
int echo = D10; // Echo pin

// 부저 핀 설정
int Buzzer = D11;

// 적외선 센서 핀 설정
int Sensor1 = D12;
int Sensor2 = D13;

int period = 6; // 몇초에 한번씩 자동 계패 할지 (주기) 60초로 설정
int cnt = 0;  //loop한번에 10초 끝날때마다 cnt증가

int flag = 0; //문 한번만 열리게하는 플레그

//문 열기
void openDoor()
{
  tone(Buzzer, 523);
  delay(500);
  noTone(Buzzer);
  delay(1000);
  servo1.write(0);
  servo2.write(180);
}

//문 닫기
void closeDoor()
{
  servo1.write(180);
  servo2.write(0);
}

//초음파 거리 값
int ultrasonicSensor()
{
  float Length, distance;
  digitalWrite(trig, LOW);
  delay(2);
  digitalWrite(trig, HIGH);
  delay(10);
  digitalWrite(trig, LOW);

  Length = pulseIn(echo, HIGH);
  distance = ((float)(340 * Length) / 10000) / 2;
  Serial.println(distance);
  return distance;
}

//문위에 쓰레기 감지
void sensingDoor()
{
  int val1 = digitalRead(Sensor1);
  int val2 = digitalRead(Sensor2); 

  if (val1 == LOW && val2 == LOW){
    openDoor();
    cnt = 0;
    delay(5000);  // 쓰레기 밑으로 내려갈떄까지 열여있는 시간
    closeDoor();
  }
  else {
    noTone(7);
    delay(100);
  }
}

void setup()
{
  //모터
  servo1.attach(motor1);
  servo2.attach(motor2);

  //모터 초기 값
  servo1.write(180);
  servo2.write(0);

  //부저
  pinMode(Buzzer, OUTPUT);

  //적외선
  pinMode(Sensor1, INPUT);
  pinMode(Sensor2, INPUT);

  //초음파
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT);

  //시리얼 통신 속도
  Serial.begin(115200);

  //wifi 설정
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
  
  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);
  config.api_key = API_KEY;

  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;
  config.database_url = DATABASE_URL;
  config.token_status_callback = tokenStatusCallback; // see addons/TokenHelper.h
  fbdo.setBSSLBufferSize(2048 /* Rx buffer size in bytes from 512 - 16384 */, 1024 /* Tx buffer size in bytes from 512 - 16384 */);

  Firebase.begin(&config, &auth);

  Firebase.reconnectNetwork(true);
}

void loop()
{
  if(flag==0)   //매니저가 문을 열었을떈 실행안되게
  {
    sensingDoor();      //하수구문 장애물 감지해서 열고 닫고 
    if(cnt >= period)     //주기별로 하수구문 열고 닫고 (1분이상 문 안열였을 시 여는거)(실제론 1시간에 한번)
    {
      openDoor();
      cnt = 0;
      delay(500);
      closeDoor();
    }
  }
  if (Firebase.ready() && (millis() - sendDataPrevMillis > 15000 || sendDataPrevMillis == 0))   //trash값 전송
  {
    sendDataPrevMillis = millis();
    Serial.printf("Set json… %s\n", Firebase.RTDB.setInt(&fbdo, "/arduino/busan/trash", ultrasonicSensor()) ? "ok" : fbdo.errorReason().c_str());
  }

  if (Firebase.ready() && Firebase.RTDB.get(&fbdo, "/arduino/busan/door"))  //door값 rtdb에서 받아오기(메니저가 앱으로 열고 대기후 매니저가 닫힘버틑 누르면 닫힘)
  {
    int doorValue = fbdo.intData();
    Serial.print("Door value changed to: ");
    Serial.println(doorValue);
    if(doorValue==1 && flag == 0) //door가 1일때 open
    {
      openDoor();
      flag = 1;
    }
    else if(doorValue==0 && flag ==1)
    {
      closeDoor();
      flag = 0;
    }
  }
  cnt++;
  delay(2000);
}