
docker build -t ms1:cn .
docker run -it --name cn_container ms1:cn

docker exec -it cn_container bash

For Employee data:
1. GET request example:
curl -X GET http://localhost:8080/employees
2. PUT request example:
curl -X PUT http://localhost:8080/employees/3 -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "ring bearer"}'
3. POST request example:
curl -X POST http://localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "gardener"}'


For Movie data:
1. GET request example:
curl -X GET http://localhost:8080/ratings/4
2. PUT request example:
curl -X PUT http://localhost:8080/users/3 -H 'Content-type:application/json' -d '{"movieId": "1", "rating": "5"}'
3. POST request example:
curl -X POST http://localhost:8080/users -H 'Content-type:application/json' -d '{"userId": "3", "movieId": "1", "rating": "5"}'



-rating input  bigger than 5 or less than 1 returns error
-you can change or add certain user's (with user id) rating record about a movie (with movie id) with a current timestamp
-If the user has already rated the movie, you cannot change ratings about the movie through POST (return error), but can change through PUT

-you have to run curl in the other terminal of the image's container, after the loading of movie and rating data are finished
container name can obtained with a command : docker ps -q
docker exec -it <container-name> bash




