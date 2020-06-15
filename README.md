# coupon 
Rest API 기반 쿠폰시스템


## Requirements
Java 1.8

Spring boot 2.3.0.RELEASE(https://spring.io/projects/spring-boot)

MariaDB 10.5.3(https://downloads.mariadb.org/mariadb/10.5.3/)

Redis server v=4.0.9+(https://redis.io/download)

Postman -v 7.25.3(https://www.postman.com/downloads/)

## Watch Out!
Update host and port in application.yml for Redis and mariaDB.

## Run
<code>mvn spring-boot:run</code>

## API Specifications
[kakaopay_postman.zip](https://github.com/Urong/task/files/4742045/kakaopay_postman.zip)



```json 
{
	"info": {
		"_postman_id": "3736863a-1143-4931-ba3b-c919bdf54c2c",
		"name": "KaKaopay_task",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "adminController",
			"item": [
				{
					"name": "generatedAndStoreCoupons",
					"event": [
						{
							"listen": "test",
							"script": {
								"id": "4212e435-52ee-43af-ae55-d2606a98363d",
								"exec": [
									""
								],
								"type": "text/javascript"
							}
						}
					],
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {
							"accept-encoding": true
						}
					},
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"count\": 100000\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://{{ip}}:{{port}}/api/admin/settings/coupon/generator",
							"protocol": "http",
							"host": [
								"{{ip}}"
							],
							"port": "{{port}}",
							"path": [
								"api",
								"admin",
								"settings",
								"coupon",
								"generator"
							]
						},
						"description": "랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요"
					},
					"response": []
				},
				{
					"name": "getExpiredCouponListWithPage",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://{{ip}}:{{port}}/api/admin/expired/coupons",
							"protocol": "http",
							"host": [
								"{{ip}}"
							],
							"port": "{{port}}",
							"path": [
								"api",
								"admin",
								"expired",
								"coupons"
							]
						},
						"description": "발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현하세요."
					},
					"response": []
				}
			],
			"protocolProfileBehavior": {}
		},
		{
			"name": "UserController",
			"item": [
				{
					"name": "createUserCoupon",
					"request": {
						"method": "POST",
						"header": [],
						"url": {
							"raw": "http://{{ip}}:{{port}}/api/users/{{userId}}/coupon",
							"protocol": "http",
							"host": [
								"{{ip}}"
							],
							"port": "{{port}}",
							"path": [
								"api",
								"users",
								"{{userId}}",
								"coupon"
							],
							"query": [
								{
									"key": "",
									"value": null,
									"disabled": true
								}
							]
						},
						"description": "생성된 쿠폰중 하나를 사용자에게 지급하는 API를 구현하세요."
					},
					"response": []
				},
				{
					"name": "getUserCoupons",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://{{ip}}:{{port}}/api/users/{{userId}}/coupons",
							"protocol": "http",
							"host": [
								"{{ip}}"
							],
							"port": "{{port}}",
							"path": [
								"api",
								"users",
								"{{userId}}",
								"coupons"
							]
						},
						"description": "사용자에게 지급된 쿠폰을 조회하는 API를 구현하세요."
					},
					"response": []
				},
				{
					"name": "updateCouponStatus",
					"request": {
						"method": "PATCH",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"couponCode\": \"fc767c1c-9e8e-59cc-af96-cc2acd1a8cfb\",\n    \"status\": \"USED\"\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://{{ip}}:{{port}}/api/users/{{userId}}/coupon/status",
							"protocol": "http",
							"host": [
								"{{ip}}"
							],
							"port": "{{port}}",
							"path": [
								"api",
								"users",
								"{{userId}}",
								"coupon",
								"status"
							]
						},
						"description": "지급된 쿠폰중 하나를 사용하는 API를 구현하세요. (쿠폰 재사용은 불가)\r\n\r\n지급된 쿠폰중 하나를 사용 취소하는 API를 구현하세요. (취소된 쿠폰 재사용 가능)"
					},
					"response": []
				}
			],
			"protocolProfileBehavior": {}
		}
	],
	"protocolProfileBehavior": {}
}
```

## License
MIT license

Copyright 2020 © youngjun.byeon

https://github.com/YoungjunByeon/task/blob/master/LICENSE.txt
