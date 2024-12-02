# CollaB

# 서버 아키텍처

![[CollaB_Server_Architecture.png]]

- - - - -   

# 사용 API
## 채팅
- POST 
- /chat/new
- 설명 : 채팅방을 생성한다. 
- 요청 :
```json
[
  {
    "userId": 0,
    "userName": "string"
  }
]
```

- GET
- /chat/room/member/{roomId}
- 설명 : roomId에 해당하는 유저들을 조회한다.
- 요청 : api 호출시 roomId를 넣어 호출
- 응답 :
```json
[
  {
    "enterDate": {
      "date": 0,
      "day": 0,
      "hours": 0,
      "minutes": 0,
      "month": 0,
      "nanos": 0,
      "seconds": 0,
      "time": 0,
      "timezoneOffset": 0,
      "year": 0
    },
    "roomId": "string",
    "sequence": 0,
    "userId": 0
  }
]
```
## 피어리뷰
- GET
- /peer-review/all/{userId}
- 설명 : 프로젝트 목록중 어떤것의 피어리뷰를 했는지, 하지 않았는지 확인하기 위함. 
- 요청 : api호출시 userId에 정수형의 인자를 사용
- 응답 : 
```json
[
  {
    "currentReviewerNumber": 0,
    "maxReviewerNumber": 0,
    "peerReviewId": 0,
    "peerReviewScore": 0,
    "projectId": 0,
    "reviewed": true,
    "userId": 0
  }
]
```

- POST
- /peer-review/do
- 설명 : 프로젝트가 끝나고 동료평가를 실행
- 요청 :
```json
{
  "projectId": 0,
  "scores": [
    {
      "score": 0,
      "userId": 0
    }
  ],
  "userId": 0
}
```
## 매칭
- POST
- /api/post/match
- 설명 : 사용자의 정보를 통해 매칭을 생성
- 요청 : 
``` json
{
  "technologyId": [
    0
  ],
  "uid1": 0
}
```

- GET
- /api/get/match-info/match/{matchId}
- 설명 : 매칭시 발생한 id를 통해 매칭을 조회
- 요청 : api 호출시 정수형의 matchId를 포함하여 호출
- 응답 : 
```json
{
  "links": {
    "empty": true
  },
  "matchId": 0,
  "matchSuccess": "FALSE",
  "matchingDate": "2024-12-02T14:00:32.891Z",
  "matchingRequest": "PENDING",
  "profile": {
    "id": 0,
    "instruction": "string",
    "member": {
      "account": "string",
      "birthday": "2024-12-02T14:00:32.891Z",
      "email": "string",
      "gender": "string",
      "id": 0,
      "major": "string",
      "name": "string",
      "password": "string",
      "phoneNumber": "string",
      "roles": [
        {
          "name": "string"
        }
      ],
      "token": "string"
    },
    "nickname": "string",
    "peer": 0,
    "personalLink": "string",
    "photo": "string",
    "projectExperience": true,
    "promise": "string",
    "role": "string"
  },
  "projectId": 0,
  "uid1": 0
}
```
## 프로젝트
- POST
- /api/post/project
- 설명 : 새로운 프로젝트를 생성
- 요청 :
``` json
{
  "chatroomId": "string",
  "projectIntroduction": "string",
  "projectManager": 0,
  "projectName": "string",
  "projectOneLineIntroduction": "string",
  "userId": 0
}
```

- GET
- /api/get/project/{projectId}/detail
- 설명 : 특정 프로젝트의 상세정보를 조회
- 요청 : api 호출시 정수형의 projectId를 포함하여 호출
- 응답 : 
```json
{
  "creationDate": "2024-12-02",
  "numberOfMembers": 0,
  "profiles": [
    {
      "id": 0,
      "instruction": "string",
      "member": {
        "account": "string",
        "birthday": "2024-12-02T13:58:21.800Z",
        "email": "string",
        "gender": "string",
        "id": 0,
        "major": "string",
        "name": "string",
        "password": "string",
        "phoneNumber": "string",
        "roles": [
          {
            "name": "string"
          }
        ],
        "token": "string"
      },
      "nickname": "string",
      "peer": 0,
      "personalLink": "string",
      "photo": "string",
      "projectExperience": true,
      "promise": "string",
      "role": "string"
    }
  ],
  "projectDeadlineDate": "2024-12-02",
  "projectFinished": true,
  "projectId": 0,
  "projectIntroduction": "string",
  "projectManager": 0,
  "projectName": "string",
  "projectOneLineIntroduction": "string",
  "projectStartDate": "2024-12-02",
  "projectStarted": true,
  "views": 0
}
```

- - - - -   

# 기대 효과
1. 기존 서비스와의 차별점
	1. 사용자 경험 개선 (스와이프, 기술스택)
		- 나의 기술스택을 프로필에 기재할 수 있고, 이를 통해 타 사용자에게 보여줄 수 있다. 이는 팀원모집 및 프로젝트 매칭시 필요한 인원과 프로젝트를 보다 편리하게 모집할 수 있다.
		- 팀원을 효율적으로 모집하기 위해 팀원매칭 기능에 스와이프기능을 도입했다. 데이트 매칭 어플 '틴더'에서 착안한 방식으로 편리하게 팀원을 찾을수 있다.
		- 이미 만들어진 프로젝트에 참가하기 위해서 조회순으로 프로젝트를 정렬하여 인기있는 프로젝트에 참여할 수 있다. 
	2. 동료평가 시스템(강수량지표, 신뢰성 강화)
		- 매 프로젝트가 종료되면 팀원들을 평가한다. 이는 적극적인 활동과 협력을 유도한다. 또한 다른 프로젝트에 참가하거나 다른 사용자가 나를 믿고 프로젝트를 진행할수 있는 지표가 된다.
		- 이 동료평가 점수를 강수량으로 표현하여 직관적으로 알아볼 수 있도록 한다.
2. 나에 대해 파악 가능
	- 본인의 실력을 정확하게 파악하지 못하는 경우, 무엇을 어떻게 시작해야하는지 못하는 경우가 많고, 어느 분야에 관심이 있는지 알 수 없다. 'CollaB'를 통해 자신의 약점과 강점을 파악하고 약점을 채워나갈수 있다. 또한 적성에 맞는 진로를 찾을수 있다.
3. 올바른 사회 구성원으로 성장 가능
	- 개발과 관련된 직무는 협업이 매우 중요하다. 'CollaB'를 통해서 지속적인 협업을 진행함으로써 개발자로서 갖춰야 할 협업능력, 창의적 사고를 키울수 있다. 동료평가를 통해 책임감을 부여하고 적극적임 참여를 유도하여 협동심을 기를 수 있게 한다. 이러한 경험은 적극적이고 책임감있는 사회구성원으로 성장하도록 도와줄 것이다.



