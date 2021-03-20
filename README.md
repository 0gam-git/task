# 사전과제 


## 핵심 문제해결 전략
1. Redis를 이용한다.(확장이 필요할 시 클러스터와 센티널을 이용)
2. 랜덤으로 돈을 분배한다.
3. 분배된 돈을 하나씩 가져가는 구조로 설계한다.
4. TTL을 이용하여 요구사항의 만료시간 제어한다.


## API 문서화 및 테스트 자동화
* Postman를 이용한다.(https://www.postman.com/)
* 위 링크에서 다운로드 후, "사전과제.postman_collection.json"를 import 한다.

![image](https://user-images.githubusercontent.com/16284971/111865033-e20d5380-89a7-11eb-8788-da7df82646c5.png)

 

## License
MIT license

Copyright 2020 © youngjun.byeon

https://github.com/YoungjunByeon/task/blob/master/LICENSE.txt
