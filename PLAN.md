# План разработки: «Погодные сведения»

Два отдельных приложения (каждое — свой Spring Boot бэкенд + свой Vue 3 SPA), общающиеся между собой через Kafka.

## Стек

- **Backend**: Spring Boot 4.0.x, Java 25, Hibernate 7 (через spring-boot-starter-data-jpa), JPA, spring-kafka, Maven multi-module
- **Frontend**: Vue 3 + Vite (SPA), общение с бэкендом по REST, сборка в jar бэкенда через `frontend-maven-plugin`
- **Инфраструктура**: Kafka (KRaft, apache/kafka) + PostgreSQL в docker-compose

## Архитектура

```
D:\ipr2026
├── pom.xml                     # parent: Java 25, Spring Boot 4.0.x BOM
├── docker-compose.yml          # Kafka + PostgreSQL (две БД)
├── docker/postgres/init/       # SQL: создание БД weather_producer, weather_consumer
├── producer/                   # Сайт 1, порт 8081
│   ├── pom.xml
│   ├── src/main/java/com/example/producer/
│   │   ├── domain/             # WeatherRecord (JPA), WindDirection (enum, 8 румбов)
│   │   ├── dto/                # WeatherRequest (валидация), WeatherResponse
│   │   ├── event/              # WeatherEvent — событие Kafka
│   │   ├── repository/         # WeatherRecordRepository
│   │   ├── service/            # WeatherService
│   │   └── web/                # WeatherController, ApiExceptionHandler
│   ├── src/main/resources/application.yml
│   └── frontend/weather-ui/    # Vue 3 + Vite
└── consumer/                   # Сайт 2, порт 8082 (та же структура)
```

- **Kafka-топик**: `weather-updates`. Продюсер шлёт JSON без type-заголовков
  (`spring.json.add.type.headers=false`), консьюмер читает через
  `ErrorHandlingDeserializer` + `JsonDeserializer` c `spring.json.value.default.type`.
- **Каждое приложение имеет свою БД** (weather_producer / weather_consumer), приложения независимы, DTO события дублируется в обоих модулях.

## Сайт 1 — producer (порт 8081)

**Backend:**
- `POST /api/weather` — валидация (`@NotNull` + диапазоны: влажность 0–100, скорость ≥ 0),
  сохранение в свою БД, отправка события в Kafka, возврат записи.
- `GET /api/weather/history` — вся история (новые сверху) для таблицы.
- Ошибки валидации → 400 + `{errors: {поле: сообщение}}`.

**Frontend (SPA, по ТЗ):**
- Блок «Погодные сведения»: температура, влажность, скорость ветра, select направления ветра
  (Север … Северо-запад), кнопка «Передать».
- Незаполненное поле → красная рамка, запрос не отправляется, больше ничего не происходит.
- Блок «История»: таблица 6 колонок — № (id записи), температура, влажность,
  скорость ветра, направление ветра, дата и время добавления.
- После успешной отправки — перезагрузка истории.

## Сайт 2 — consumer (порт 8082)

**Backend:**
- `@KafkaListener` на `weather-updates` → сохранение в свою БД (JPA).
- `GET /api/weather/latest` — последняя запись (204, если данных нет).
- `GET /api/weather/{id}` — запись по id.

**Frontend (SPA, по ТЗ):**
- Крупные цифры: температура; мельче — влажность.
- Клик по блоку → раскрываются подробности: дата и время добавления, температура,
  влажность, направление ветра.
- Обновление: polling `GET /latest` каждые 3 секунды.

## Дополнительный функционал: абсолютная влажность

Масса водяного пара в 1 м³ воздуха (г/м³), **расчёт на клиенте сайта 2** из T и RH
(формула Магнуса):

```
e_s = 6.112 · e^(17.62·T / (243.12 + T))      — давление насыщенного пара, гПа
e   = (RH / 100) · e_s                         — фактическое давление пара, гПа
AH  = 216.7 · e / (273.15 + T)                 — абсолютная влажность, г/м³
```

- Отображается в крупном блоке (мельче температуры/влажности) и в подробностях, подпись «г/м³».
- Утилита `consumer/frontend/weather-ui/src/utils/humidity.js` + тест на Vitest
  (контрольные точки: T=20, RH=50 → ≈8.6 г/м³; T=30, RH=100 → ≈30.3 г/м³; T=0, RH=100 → ≈4.8 г/м³).

## Инфраструктура

- `docker-compose.yml`: `apache/kafka` (KRaft, один брокер, host-Listener localhost:9092),
  `postgres:17` (пользователь/пароль weather/weather), init-скрипт создаёт две БД.
- Приложения подключаются: `localhost:9092` и `localhost:5432` (переопределяется переменными окружения).

## Этапы

1. Скелет: parent pom, модули, docker-compose.
2. Producer backend.
3. Consumer backend.
4. Producer frontend.
5. Consumer frontend (+ Vitest).
6. Интеграция `frontend-maven-plugin`, `mvn clean verify`.
7. End-to-end прогон по ТЗ (валидация, «Передать», история, событие в Kafka, сайт 2, абсолютная влажность).

## Как запустить

```bash
docker compose up -d                      # Kafka + PostgreSQL
mvn clean package                         # сборка обоих jar (с фронтендами)
java -jar producer/target/weather-producer-1.0.0.jar    # сайт 1 → http://localhost:8081
java -jar consumer/target/weather-consumer-1.0.0.jar    # сайт 2 → http://localhost:8082
```

Режим разработки фронтенда: `npm run dev` в `*/frontend/weather-ui` (Vite проксирует /api на свой бэкенд).

## Допущения

- «Номер» в первой колонке истории — id записи из БД.
- Даты генерируются на бэкенде producer'а и едут внутри Kafka-события.
- Серверная валидация добавляет разумные диапазоны (влажность 0–100, скорость ≥ 0),
  фронтенд проверяет только заполненность — строго по ТЗ.
