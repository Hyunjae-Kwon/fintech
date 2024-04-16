## 📝 Trouble Shooting

---

 - 프로젝트를 진행하면서 발생한 문제점들과 해결법을 서술합니다.
 - 이전 발생한 문제들에 대한 기록을 하지 못했다.. 이제부터라도 잘 기록해보려고 한다.

### [20240415] 소유 계좌 조회시 발생한 에러

---

 - 문제 현상 : AccountService 의 getAllAccount 메소드 내용 중 
    Page<AccountEntity> accounts 에 저장된 내용이 없음
 - 에러 메세지 : Page 2 of 1 containing unknown instances
 - 발생 시기 : 계좌 조회 실행 시 발생 (에러 코드 발생은 아니지만 결과 content 에 표시되는 내용이 없음)

#### 해결 방법
1. page 를 전달하는 파라미터의 default 값 1 -> 0 으로 수정 후 해결

#### 해당 문제를 해결하면서 배운 점

 - 페이지 조회 시 0페이지부터 호출해야한다.