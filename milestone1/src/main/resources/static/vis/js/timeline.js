// jQuery client that consumes a Spring-based RESTful web service

class timeline {
    constructor() {
        this.year = null;
    }
    
    defaultShowTimeline() {
        this.showTimeline('http://localhost:8080/movieInfo/csv/timeline');
    }

    showOneTimeline(url) {
        d3.csv(url)
            .then(data => {
                const margin = { top: 5, right: 30, bottom: 50, left: 120 },
                width = 900 - margin.left - margin.right,
                height = 700 - margin.top - margin.bottom;
        
                var xScale = d3.scaleLinear().domain(d3.extent(data, (d) => d.year)).range([0, width]);
                var yScale = d3.scaleLinear().domain([0, d3.max(data, (d) => parseFloat(d.count))*1.5]).range([height, 0]);

                d3.select("#timeline").select("svg").remove()
                const svg = d3.select("#timelinechart")
                    .append("svg")
                    .attr('width', width + margin.left + margin.right)
                    .attr('height', height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", `translate(${margin.left},${margin.top})`);
                
                svg.append("g")
                    .attr('class', 'x-axis')
                    .attr('transform', `translate(0, ${height})`)
                    .call(d3.axisBottom(xScale));
            
                svg.append("g")
                    .attr('class', 'y-axis')
                    .call(d3.axisLeft(yScale));
            
                svg.append("path")
                    .datum(data)
                    .style("fill", "#3e4444")
                    .attr("stroke", "white")
                    .attr("stroke-width", 0.2)
                    .attr("d", d3.area()
                                .x((d) => xScale(d.year))
                                .y0(_ => height)
                                .y1((d) => yScale(d.count)))
            })
    }
    showTimeline(url) {
        var check = 0
        d3.csv(url)
        .then(data => {
            // timeline keys
            var keys = data.columns.slice(1);
        
            const margin = { top: 5, right: 30, bottom: 50, left: 120 },
                width = 900 - margin.left - margin.right,
                height = 700 - margin.top - margin.bottom;
        
            var xScale = d3.scaleLinear().domain(d3.extent(keys)).range([0, width]);
            var max;
            if (data.length == 1) {
                max = 1.5 * d3.max(data, (d) => d3.max(Object.values(d).slice(0, 82), (a) => parseFloat(a)))
            } else max = 350;
        
            var yScale = d3.scaleLinear().domain([0, max]).range([height, 0]);
            
            // stack
            var stacked = [keys.slice().fill(0)]
            for (const d of data) {
                if (d != "columns") {
                    var len = stacked.length
                    var tempArr = stacked[len-1].slice()
                    var i = 0
                    for (const time of keys) {
                        tempArr[i] = parseFloat(tempArr[i]) + parseFloat(d[`${time}`])
                        i++
                    }
                    stacked.push(tempArr)
                }
            }
        
            var colors = ["#3e4444", "#FF5733", "#50C878"]
            // area accessor
            var genreTimeline = d3.area()
                                    .x((d) => xScale(parseInt(d[0])))
                                    .y0((d) => yScale(parseFloat(d[2])))
                                    .y1((d) => yScale(parseFloat(d[1]) + parseFloat(d[2])))

            d3.select("#timeline").select("svg").remove()
            const svg = d3.select("#timelinechart")
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
        
            const streamGraph = svg.append("g")
                .selectAll("path")
                .data(data)
                .enter()
                .append("path")
                .attr("d", (d, i) => genreTimeline(d3.zip(keys, Object.values(d).slice(0,82), stacked[i])))
                .attr("fill", (_, i) => colors[(i)%3])
                .attr("stroke", "white")
                .attr("stroke-width", 0.2)
                .on("mouseover", showTooltip)
                .on("mousemove", moveTooltip)
                .on("mouseleave", hideTooltip)

            const tooltip = d3.select("#timeline")
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
                .html("Genre: " + d.genre)
                .style("left", (d3.pointer(event)[0] + 138) + "px")
                .style("top", (d3.pointer(event)[1] + 35) + "px")
        }

        function moveTooltip(event, d) {
            tooltip
                .style("left", (d3.pointer(event)[0] + 138) + "px")
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
        });
    }
}
