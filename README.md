# Drugbox - Backend
Our project is a service that assists in the management of various pharmaceuticals within households and helps properly dispose of expired medications.

We provide guidelines for managing the inventory and expiration dates of over-the-counter and prescription medicines, as well as instructions on how to dispose of waste pharmaceuticals according to prescribed methods.

By utilizing this service, we aim to address various issues caused by improper disposal methods and, ultimately, reduce unnecessary consumption of pharmaceuticals to promote the well-being and health of individuals and the environment.
<br/><br/>

## ✔️ How to Start
### 1) Prerequisites
* Java 11
* IntelliJ IDEA or eclipse
* MySQL
* Redis

### 2) Clone
Clone this repo to your local machine using:  
```
git clone https://github.com/2024-Google-Solution-Challenge-Team5/Backend.git
```
### 3) Setup

<details><summary>resources folder structure </summary> 


![image](https://github.com/2024-Google-Solution-Challenge-Team5/Backend/assets/101239440/4069a702-d458-4cbe-b85d-3734b06e610b)
</details>




- Add `application.properties` in `resources`
  - Fill in the blank space after the equal sign with your own words
  - <details><summary>application.properties </summary> 

    ```
    # database
    application.spring.datasource.url=jdbc:mysql://localhost:3306/drugbox?createDatabaseIfNotExist=true&characterEncoding=UTF-8
    application.spring.datasource.username=
    application.spring.datasource.password=

    # GCP
    application.spring.cloud.gcp.storage.credentials.location=classpath:google-cloud.json
    application.spring.cloud.gcp.storage.project-id=
    application.spring.cloud.gcp.storage.bucket=
    application.spring.cloud.gcp.geocodingAPI=

    # Oauth - google
    spring.security.oauth2.client.registration.google.client-id=
    spring.security.oauth2.client.registration.google.client-secret=
    spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/auth/redirect/google

    # Drug Database
    application.spring.api.url=http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList
    application.spring.api.key=rsbkswEZaOZIDED3uNDy6FGvvXfmixuSLvKgzRWPIrzgqRjyHrYOrMnuNdh00HkHnBnqYOwpDjlqiklnucfJog%3D%3D

    # jwt 
    application.jwt.secret=
    application.jwt.secret_refresh=
    application.jwt.access_token.duration=
    application.jwt.refresh_token.duration=

    # redis
    spring.data.redis.host=localhost
    spring.data.redis.port=6379

    ```
    </details>
<br/>

- Add `seoul.geojson` and `drugbin.CSV` in `resources`
  - [seoul.geojson (zip file)](https://github.com/2024-Google-Solution-Challenge-Team5/Backend/files/14396826/seoul_geojson.zip)
  - [drugbin.CSV](https://github.com/2024-Google-Solution-Challenge-Team5/Backend/files/14396814/drugbin.CSV)


  - Note: This service currently only provides the locations of pharmaceutical waste collection bins in some regions of South Korea. Since we are retrieving the locations of pharmaceutical waste collection bins using an open API, exceeding the daily quota of requests may result in access restrictions.

<br/>

- Add `google-cloud.json` in `resources`
  - Private key issued by GCP (If you don't want to use image-related APIs, it's okay to just create a file and leave the file content empty)

<br/>

- Add `google-oauth.json` in `resources`
  - private key from GCP for google login

<br/>

- Create a database called `drugbox` in MySQL 
- Change the value of the `ddl-auto` variable inside `application.yml` to `create` only on the first execution.
- Run `DrugboxApplication`
<br/>
<br/>

## 🛠 Tech Stacks
- Gradle
- Spring Boot 2.7.5
- Spring Data JPA
- Spring Security
- GCP : Google Compute Engine, Google Cloud Storage, Google Cloud SQL
- JWT
- MySQL
- Redis


<br/>

## 👥 Contributors
- [Bomin Kwon](https://github.com/pingowl)
- [Miju Kim](https://github.com/miju0515)

<br/>

## 📎 Link
- [Mobile Repository](https://github.com/2024-Google-Solution-Challenge-Team5/mobile-ios-native)
- [AI Repository](https://github.com/2024-Google-Solution-Challenge-Team5/AI-MLDL)