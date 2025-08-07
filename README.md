  - 스마트 도시 홍수 방지 APP

  시스템 설명 요약

  - 조건에 맞는 상황 발생 시 하수구 문을 자동으로 개폐하여 하수구 위에 쌓여있는 쓰레기는 아래 그물로 투하
  - 그물의 포화도를 관리자가 실시간으로 확인할 수 있고, 일정이상 그물에 쓰레기가 찼을 시 관리자에게 수거 알람 발송

  얻는 이득

  - 홍수 방지
  [관련 기사 링크](https://www.hankookilbo.com/News/Read/A2022081215070000668)
  - 하수구 쓰레기 막힘 방지 (담배꽁초, 나뭇잎, 나뭇가지 등)
  [관련 기사 링크](https://n.news.naver.com/article/001/0014744121?sid=102)
  - 기존 시스템과 대비된 반자동화 시스템
  [기존 시스템 링크](https://me.go.kr/home/web/board/read.do?pagerOffset=0&maxPageItems=10&maxIndexPages=10&searchKey=&searchValue=&menuId=&orgCd=&boardId=1672610&boardMasterId=1&boardCategoryId=&decorator=)

  <br> APP 실행 초기화면 <br>
  ![image](https://github.com/user-attachments/assets/2fe39c2b-0586-4b10-ab73-ee67a7ad7d61)


  - 네이버 지도 UI를 활용하여 하수구의 실제 위치를 마커로 표시하고 클릭하여 관리할 수 있게 구현

  <br> 마커를 눌렀을 때 실행화면 <br>
  ![image](https://github.com/user-attachments/assets/7c1b5779-25b8-4b98-a092-6acce32ae17f)


  - 적외선 센서로 그물에 투하된 쓰레기의 포화량을 확인하여 관리자에게 실시간으로 확인할 수 있게 구현
  - 그물을 수거하거나 특정 상황을 위해 수동으로 하수구 문을 개폐할 수 있도록 구현
    
  <br> 시스템 구조도 <br>
  ![image](https://github.com/user-attachments/assets/98661792-f287-4a1e-9052-dd1eb87706ec)

  <br> 프로토타입 이미지 <br>
  ![image](https://github.com/user-attachments/assets/82f8ce00-d179-44b1-ab71-e8ea10ec7592)

 
 
  개선 할 사항

  - 하수구에 GPS를 설치하여 다수의 하수구 좌표값을 입력받아 마커로 표시하는 기능
