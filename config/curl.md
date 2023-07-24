# Get all meals:
curl http://localhost:8080/topjava/rest/meals
# Get meal by id:
curl http://localhost:8080/topjava/rest/meals/100007
# Delete meal:
curl --request DELETE http://localhost:8080/topjava/rest/meals/100008
# Update meal:
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100004' \
--header 'Content-Type: application/json' \
--data '    {
"id": 100004,
"dateTime": [
2020,
1,
31,
2,
0
],
"description": "Ужин",
"calories": 510
}'
# Get meals filtered by date and time:
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-31&startTime=00%3A00&endTime=10%3A00'
# Create new meal:
curl --location 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '    {
"dateTime": [
2023,
1,
31,
2,
0
],
"description": "Ужин",
"calories": 510
}'