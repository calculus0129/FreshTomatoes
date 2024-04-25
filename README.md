
```
docker build -t ms1:cn .
docker run -it --name cn_container ms1:cn
docker exec -it cn_container bash
```

or alternatively, run `sh docker.sh`.

Key Features

1. ?
2. Temporal Visualization of movie genres and movies
   1- auto complete (recommendation for genre keyword)
      - Examples:
         - `curl http://localhost:8080/movieInfo/genres?s=F`
         - Output: list of genre name start with "F"
         - `curl http://localhost:8080/movieInfo/titles?s=Toygenres?s=Documentarya`
         - Output: empty list
   2- auto complete (recommendation for movie keyword)
        - Examples:
           - `curl http://localhost:8080/movieInfo/titles?s=Toy%20Sto`
           - Output: list of movie titles starting with "Toy Sto"
   3- export csv for visualization
      1) export csv for genre timeline
      - `curl http://localhost:8080/movieInfo/csv/timeline`
      2) export csv for one genre timeline (e.g. Sci-Fi and Action)
      - `curl http://localhost:8080/movieInfo/csv/timeline/one?g=Sci-Fi`
      - `curl http://localhost:8080/movieInfo/csv/timeline/one?g=Action`
      3) export csv for movie list sorted with increasing rating numbers released in the requested year (e.g. 1970)
      - `curl -X GET http://localhost:8080/movieInfo/csv/year?y=1970`
      4) export csv for movie list sorted with increasing rating numbers released in the same year with the requested movie (e.g. Toy Story (1995))
      - `curl -X GET http://localhost:8080/movieInfo/csv/year?t="Toy%20Story%20(1995)"`
    
4. Recommendation via User Information
   - List the movies rated by the users with the given user information(gender, age, occupation), sorted in averageRating-decreasing, reviewNumber-decreasing, and movieTitle lexicographic order.
   - Examples:
     - `curl "http://localhost:8080/recommend/user?gender=F&age=18&occ=20"`
       - Output: 1483 of movie data sorted.
     - `curl "http://localhost:8080/recommend/user?gender=M&age=18&occ=13"`
       - Output: []. That's because there are no such user rating information.

For Movie data:
1. GET request example:
   - `curl http://localhost:8080/movies` (Search all movies)
   - `curl http://localhost:8080/movies/1` (Search movies with a movieID)
2. PUT request example:
   - `curl -X PUT http://localhost:8080/movies/1 -H 'Content-type:application/json' -d '{"movieId":1, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}'`
3. POST request example:
   - `curl -X POST http://localhost:8080/movies -H 'Content-type:application/json' -d '{"movieId":4001, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}'`
4. DELETE request example:
   - `curl -X DELETE http://localhost:8080/movies/4001`

You can add a movie whose movieID is not yet in the list. If there is a movie with the same movieID, then it returns a response of an error message.
```
root@98d786015c03:~/project# curl -X POST http://localhost:8080/movies -H 'Content-type:application/json' -d '{"movieID":4001, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}'
{"title":"Ricochet Love","genres":"Fantasy|Romance"}
root@98d786015c03:~/project# curl -X POST http://localhost:8080/movies -H 'Content-type:application/json' -d '{"movieID":4001, "title":"Ricochet Love", "genres":["Romance","Fantasy"]}'
A movie with such a movieID already exists in the movie repository!
```

For Rating data:
1. GET request example:
   - `curl http://localhost:8080/ratings` (Search all movies)
   - `curl "http://localhost:8080/ratings?movieId=1&max=5"` (Search ratings with a movieID)
   - `curl "http://localhost:8080/ratings?userId=6&movieId=1"` (Search ratings with both a userId and a movieId.)
2. PUT request example:
   - `curl -X PUT "http://localhost:8080/ratings?userId=6&movieId=1" -H "Content-Type: application/json" -d '{"rating": 3, "timestamp": 999999999}'`
3. POST request example:
   - `curl -X POST "http://localhost:8080/ratings" -H "Content-Type: application/json" -d '{"userId":7000, "movieId":1, "rating": 5, "timestamp": 922222222}'`
4. DELETE request example:
   - `curl -X DELETE "http://localhost:8080/ratings?userId=7000&movieId=1"`


To test,

run `sh test.sh` locally.\
Then you can locate from the file
`projects-group3/milestone1/target/site/jacoco/index.html`
to see the branch coverage.


-rating input  bigger than 5 or less than 1 returns error
-you can change or add certain user's (with user id) rating record about a movie (with movie id) with a current timestamp
-If the user has already rated the movie, you cannot change ratings about the movie through POST (return error), but can change through PUT

-you have to run curl in the other terminal of the image's container, after the loading of movie and rating data are finished
container name can obtained with a command : docker ps -q
docker exec -it <container-name> bash




