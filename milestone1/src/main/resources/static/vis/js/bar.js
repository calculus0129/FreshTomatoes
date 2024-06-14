class bar {

    constructor() {}

    defaultShowBar() {
        this.showBar("http://localhost:8080/movieInfo/csv/year?y=1970");
    }

    showBar(url) {
        d3.dsv("\\", url)
            .then(data => {
                const margin = { top: 5, right: 30, bottom: 50, left: 120 },
                width = 900 - margin.left - margin.right,
                height = 700 - margin.top - margin.bottom;
                
                const ticks = data.length;
            
                var xScale = d3.scaleLinear().domain([0, ticks]).range([0, width]);
                var yScale = d3.scaleLinear().domain([0, 1.1 * d3.max(data, (d) => parseFloat(d.count))]).range([height, 0]);
            
                d3.select("#bars").select("svg").remove()
                const svg = d3.select("#barchart")
                    .append("svg")
                    .attr('width', width + margin.left + margin.right)
                    .attr('height', height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", `translate(${margin.left},${margin.top})`);

                const xAxis = svg.append("g")
                    .attr('class', 'x-axis')
                    .attr('transform', `translate(0, ${height})`)
                    .call(d3.axisBottom(xScale));
            
                const yAxis = svg.append("g")
                    .attr('class', 'y-axis')
                    .call(d3.axisLeft(yScale));
            
                svg.append("g")
                        .attr("fill", "#FF5733")
                        .attr("stroke", "white")
                        .attr("stroke-width", 0.2)
                    .selectAll("rect")
                    .data(data)
                    .enter()
                    .append("rect")
                        .attr("x", (_, i) => xScale(i))
                        .attr("y", (d) => yScale(parseInt(d.count)))
                        .attr("width", width/ticks*0.8)
                        .attr("height", (d) => height - yScale(parseInt(d.count)))
                        .on("mouseover", showTooltip)
                        .on("mousemove", moveTooltip)
                        .on("mouseleave", hideTooltip)

                const tooltip = d3.select("#barchart")
                    .append("div")
                    .style("opacity", 0)
                    .attr("class", "tooltip")
                    .style("background-color", "black")
                    .style("border-radius", "5px")
                    .style("padding", "10px")
                    .style("color", "white")
                    .style("display", "inline")
                    .style("position", "fixed")
                    .style("pointer-events", "none")
        
                    // Tooltip functions
                function showTooltip(event, d) {
                    tooltip
                        .transition()
                        .duration(10)
                        .style("opacity", 1)
                    tooltip
                        .html("Title: " + d.title +
                            "<br>Number of reviews: " + d.count +
                            "<br>Genres: " + d.genres)
                        .style("left", (d3.pointer(event)[0] + 138 + width) + "px")
                        .style("top", (d3.pointer(event)[1]) + "px")
                }
        
                function moveTooltip(event, d) {
                    tooltip
                        .style("left", (d3.pointer(event)[0] + 138 + width) + "px")
                        .style("top", (d3.pointer(event)[1] + 35) + "px")
                }
        
                function hideTooltip(event, d) {
                    tooltip
                        .transition()
                        .duration(200)
                        .style("opacity", 0)
                }
            }).catch(function(error) {
                check = 1
            })
    }
}
  


