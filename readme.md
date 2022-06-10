# UCI CS237 22 Spring Project
## Middleware for a city-wise real-time product tracking system

### Backend:

- run Zookeeper first
- run Kafka
- run Spring Java Backend "`KafkaJavaApplication`"
- go to `http://localhost:8080/kafka/produce_random` to start producing

### Frontend:
You need to have node.js installed first.
`cd` to `/CS237-GUI`

- run `npm install`
- run Angular CLI Server (`ng serve` or use IDEA's automatically generated configuration, but first add `CS237-GUI` as a project module)
- go to `localhost:8080`
