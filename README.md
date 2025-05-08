# bilyoner-live-betting


### Genel mimari

- **Chain of Responsibility:** Oran güncellemeleri zincir şeklinde işlenir (RandomOddsHandler → CacheOddsHandler → WebSocketOddsHandler → DatabaseOddsHandler → KafkaOddsHandler). Bu sayede yeni bir adım eklemek veya çıkarmak çok kolaydır.
- **Event Handler Zinciri:** Event işlemleri de benzer şekilde zincirlenir (ör. event cache, websocket, db).
- **Event-Driven ve Gerçek Zamanlılık:** Kafka ve WebSocket ile hem ölçeklenebilir hem de gerçek zamanlı bir yapı sağlanır.
- **Circuit Breaker:** Bet işlemlerinde resilience için Resilience4j ile circuit breaker uygulanır.
- **Parametrik Timeout:** Bahis işlemlerinde timeout süresi application.properties üzerinden değiştirilebilir.
- **Swagger/OpenAPI:** Tüm endpointler otomatik olarak dokümante edilir.

---

## Kurulum ve Çalıştırma

1. **Projeyi Klonla:**
   ```sh
   git clone <repo-url>
   cd bilyoner
   ```

2. **Bağımlılıkları Yükle ve Uygulamayı Başlat:**
   ```sh
   ./gradlew bootRun
   # veya
   ./mvnw spring-boot:run
   ```

3. **H2 Console:**  
   [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
   (JDBC URL: `jdbc:h2:mem:bettingdb`, kullanıcı: `sa`, şifre: `sa`)

4. **Swagger UI:**  
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## API Dökümantasyonu

### 1. **Bülten Yönetimi**

#### Maç Ekle
- **POST** `/api/events`
- **Body:**
  ```json
  {
    "leagueName": "Süper Lig",
    "homeTeam": "Galatasaray",
    "awayTeam": "Fenerbahçe",
    "homeWinOdds": 1.85,
    "drawOdds": 3.20,
    "awayWinOdds": 2.10,
    "startTime": "2024-06-30T20:00:00"
  }
  ```

#### Aktif Bülteni Görüntüle
- **GET** `/api/events/active`
- **Yanıt:** Liglere göre gruplanmış, oranları güncel maç listesi.

---

### 2. **Bahis İşlemleri**

#### Tekli Bahis Yap
- **POST** `/api/bets`
- **Header:** `Customer-ID: 123`
- **Body:**
  ```json
  {
    "eventId": 1,
    "betType": "HOME_WIN",
    "multipleCount": 2,
    "stakeAmount": 50.0,
    "oddsAtBet": 1.85
  }
  ```
  /api/events/{id} (yani event’i ID ile getiren) bir GET

#### Kombine (Çoklu) Kupon Yap
- **POST** `/api/bets/betslip`
- **Header:** `Customer-ID: 123`
- **Body:**
  ```json
  {
    "bets": [
      { "eventId": 2, "betType": "HOME_WIN", "oddsAtBet": 2.47 },
      { "eventId": 1, "betType": "HOME_WIN", "oddsAtBet": 2.10 }
    ],
    "stakeAmount": 100.0,
    "multipleCount": 3
  }
  ```
- **Örnek Yanıt (Oran Değişikliği Durumu):**
  ```json
  {
    "success": false,
    "results": [
      {
        "eventId": 2,
        "betType": "HOME_WIN",
        "status": "OK",
        "currentOdds": null,
        "message": null
      },
      {
        "eventId": 1,
        "betType": "HOME_WIN",
        "status": "ODDS_CHANGED",
        "currentOdds": 2.47,
        "message": "Oran değişti, lütfen güncel oranla tekrar deneyin!"
      }
    ]
  }
  ```
- **Açıklama:**  
  - Eğer kuponda bir maçın oranı değişmişse, ilgili maç için `"status": "ODDS_CHANGED"` ve `"currentOdds"` alanı ile birlikte hata mesajı döner.
  - Limit aşımı, timeout, inaktif event gibi durumlarda da benzer şekilde detaylı hata mesajları döner.

---

### 3. **Gerçek Zamanlı Oran Takibi**

- **WebSocket Endpoint:** `/ws`
- **Topic:** `/topic/events`
- **Açıklama:** Oranlar her güncellendiğinde frontend’e anlık push edilir.

---

### 4. **Limitler ve Kurallar**

- Bir maça toplamda maksimum **500** çoklu kupon oynanabilir.
- Bir kuponda maksimum **10.000 TL** yatırım yapılabilir.
- Bahis işlemi başlatıldığında alınan oran, işlem tamamlanana kadar korunur.
- Her bahis işlemi için **2 saniye** timeout uygulanır (parametrik).
- Oran değişikliği, limit aşımı, timeout gibi tüm iş kuralları için detaylı hata mesajları ve HTTP status kodları döner.

---

### 5. **Test ve Güvenlik**

- **Birim Testleri:** `src/test/java` altında örnek testler mevcut.
- **Mock Authentication:** Gerçek kullanıcı doğrulaması yok, `Customer-ID` header ile işlem yapılır.
- **Hata Yönetimi:** Tüm iş kuralları için detaylı hata mesajları ve HTTP status kodları döner.

---

## Parametrik Ayarlar (application.properties)

```properties
bet.timeout.seconds=2
app.betting.max-bets-per-event=500
app.betting.max-stake-amount=10000
app.scheduling.odds-update-interval=1000
spring.h2.console.enabled=true
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```


