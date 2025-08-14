# NOAH 

NOAH는 센서를 활용한 쓰레기 감지, 자동 문 개폐 기능, 그리고 앱을 통한 하수구 상태 실시간 모니터링 기능을 구현한 IoT 기반 시스템입니다.

---

## 주요 기능

- **자동 하수구 문 개폐**
  - 조건에 맞는 상황 발생 시 하수구 문 자동 개폐
  - 하수구 위 쓰레기는 아래 그물로 투하
  - 쓰레기 포화도에 따라 관리자에게 수거 알람 발송

- **실시간 상태 모니터링**
  - 적외선 센서 및 초음파 센서를 활용하여 하수구 쓰레기 유무 및 적재량 감지
  - Arduino와 앱 간 실시간 데이터 동기화
  - 관리자 앱에서 하수구별 상태 확인 및 수동 개폐 제어 가능

- **위치 기반 관리**
  - Naver Map API를 활용하여 하수구 위치를 마커로 표시
  - 마커 클릭 시 하수구 상태 확인 및 관리 가능
  - 향후 GPS 설치를 통한 다수 하수구 관리 개선 계획

---

## 사용 기술

- **Embedded Platform:** Arduino UNO  
- **Sensors:** 적외선 센서, 초음파 센서  
- **Communication Module:** ESP8266 (Wi-Fi)  
- **Frontend:** Android Studio  
- **Backend:** Arduino - Firebase  
- **Database:** Firebase Realtime Database  

---

## 시스템 구조

1. Arduino 기반 임베디드 시스템에서 센서 데이터 수집  
2. 서보 모터 제어를 통한 하수구 자동 개폐 기능 구현  
3. ESP8266 Wi-Fi 모듈을 이용하여 Arduino와 Firebase 간 실시간 통신  
4. Firebase Realtime Database를 통해 센서값, 개폐 상태 등 관리 및 앱과 동기화  
5. Android Studio 기반 앱에서 하수구 상태 조회 및 수동 제어 가능  

---

## 화면 및 구조 예시

**APP 실행 초기화면**  
![image](https://github.com/user-attachments/assets/2fe39c2b-0586-4b10-ab73-ee67a7ad7d61)

**마커 선택 시 실행 화면**  
![image](https://github.com/user-attachments/assets/7c1b5779-25b8-4b98-a092-6acce32ae17f)

**시스템 구조도**  
![image](https://github.com/user-attachments/assets/98661792-f287-4a1e-9052-dd1eb87706ec)

**프로토타입 이미지**  
![image](https://github.com/user-attachments/assets/82f8ce00-d179-44b1-ab71-e8ea10ec7592)

---

## 시스템 시연 영상

[하수구 위에 쓰레기가 있을 때 자동으로 하수구를 여는 영상](https://youtube.com/shorts/3Wmb6r4MecU?feature=share)

[쓰레기가 다 차서 앱으로 하수구를 여는 영상](https://youtube.com/shorts/XITCrnQTSnc?feature=share)

---

## 기대 효과

- 하수구 막힘 방지 및 홍수 예방  
  - [관련 기사 1](https://www.hankookilbo.com/News/Read/A2022081215070000668)  
  - [관련 기사 2](https://n.news.naver.com/article/001/0014744121?sid=102)  
- 기존 반자동화 시스템 대비 효율 향상  
  - [기존 시스템 링크](https://me.go.kr/home/web/board/read.do?pagerOffset=0&maxPageItems=10&maxIndexPages=10&searchKey=&searchValue=&menuId=&orgCd=&boardId=1672610&boardMasterId=1&boardCategoryId=&decorator=)
