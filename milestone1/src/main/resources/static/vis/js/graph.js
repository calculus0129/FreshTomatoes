
const bargraph = new bar();
const timelinegraph = new timeline();

$(document).ready(function() {
    bargraph.defaultShowBar();
    timelinegraph.defaultShowTimeline();
});

function autoGenre() {
    var genre = document.getElementById("myGenre").value;
    console.log(genre)
    $.ajax({
        dataType: "JSON",
        url: `http://localhost:8080/movieInfo/genres?s=${genre}`,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log(data);
            autoGenre_helper(data);
        }
    })
}

function autoGenre_helper(data) {
    removeAuto();
    var genre = data;
    var dropdownContent = document.getElementById("myGenreList");
    genre.forEach(function (d) {
        var link = document.createElement("a")
        link.className = "auto";
        link.textContent = d
        link.href = "#"
        link.onclick = function () {
            document.getElementById("myGenre").value = d
        };
        dropdownContent.appendChild(link);
    });
}
function removeAuto() {
    d3.selectAll(".auto").remove();
}

function newTimeline() {
    removeAuto();
    var genre = document.getElementById("myGenre").value 
    var url = `http://localhost:8080/movieInfo/csv/timeline/one?g=${genre}`
    timelinegraph.showOneTimeline(url)
}

function defaultTimeline() {
    timelinegraph.defaultShowTimeline();
}

function autoTitle() {
    var title = document.getElementById("myTitle").value;
    console.log(title)
    $.ajax({
        dataType: "JSON",
        url: `http://localhost:8080/movieInfo/titles?s=${title}`,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log(data);
            autoTitle_helper(data);
        }
    })
}

function autoTitle_helper(data) {
    removeAuto();
    var genre = data;
    var dropdownContent = document.getElementById("myMovieList");
    genre.forEach(function (d) {
        var link = document.createElement("a")
        link.className = "auto";
        link.textContent = d
        link.href = "#"
        link.onclick = function () {
            document.getElementById("myTitle").value = d
        };
        dropdownContent.appendChild(link);
    });
}

function newBar() {
    removeAuto();
    var title = document.getElementById("myTitle").value 
    var url = `http://localhost:8080/movieInfo/csv/year?t=${title}`
    bargraph.showBar(url)
}

function defaultBar() {
    bargraph.defaultShowBar();
}