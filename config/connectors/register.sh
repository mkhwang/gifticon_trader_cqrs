# Gifticon 커넥터 등록
curl -X POST -H "Content-Type: application/json" --data @postgres-gifticon-connector.json http://localhost:8083/connectors

# Tag 커넥터 등록
curl -X POST -H "Content-Type: application/json" --data @postgres-tag-connector.json http://localhost:8083/connectors

# Review 커넥터 등록
curl -X POST -H "Content-Type: application/json" --data @postgres-review-connector.json http://localhost:8083/connectors