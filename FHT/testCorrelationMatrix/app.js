(function(){
  //UI configuration
  var itemSize = 2,
      cellSize = itemSize - 0.2,      
      width = 800,
      height = 800,
      margin = {top:20, right:40, bottom:20, left:40};

  var data = null;
  var colorCalibration = ['#f6faaa','#FEE08B','#FDAE61','#F46D43','#D53E4F','#9E0142'];
  var horizontalCalibration = ['#f6faaa','#FEE08B','#FDAE61','#F46D43','#D53E4F','#9E0142'].reverse().concat(colorCalibration);

  //axises and scales
  var axisWidth = itemSize*256;
  var axisHeight = itemSize*256;
  var xAxisScale = d3.scale.linear()
      .range([0,axisWidth])
      .domain([0,255]);
  var xAxis = d3.svg.axis()
      .orient('top')
      .ticks(8);
      //~ .ticks(d3.time.days,3),
      //~ .tickFormat(monthDayFormat),
  var yAxisScale = d3.scale.linear()
      .range([0,axisHeight])
      .domain([0,255]);
  var yAxis = d3.svg.axis()
      .orient('left')
      .ticks(8)
      .scale(yAxisScale);
      //~ .ticks(5)
      //~ .tickFormat(d3.format('02d'))

  // annotation rect size
  var anonWidth = itemSize*256;
  var anonHeight = 20;
  var anonRectWidth = anonWidth/colorCalibration.length;
  
  // anontation y axis
  var anonyAxisScale = d3.scale.linear()
      .range([0, anonWidth])
      .domain([0,1]);
  var anonyAxis = d3.svg.axis()
      .orient('right')
      .tickValues([0, 1.0/6.0, 2.0/6.0, 3.0/6.0, 4.0/6.0, 5.0/6.0, 1.0])
      .tickFormat(d3.format('.2r'))
      .scale(anonyAxisScale);

  // anontation y axis
  var anonxAxisScale = d3.scale.linear()
      .range([0, anonWidth])
      .domain([-1,1]);
  var anonxAxis = d3.svg.axis()
      .orient('down')
      .tickValues([-1.0, -5.0/6.0, -4.0/6.0, -3.0/6.0, -2.0/6.0, 0, -1.0/6.0, 1.0/6.0, 2.0/6.0, 3.0/6.0, 4.0/6.0, 5.0/6.0, 1.0])
      .tickFormat(d3.format('.2r'))
      .scale(anonxAxisScale);

  // create heat map
  var svg = d3.select('[role="heatmap"]')
    .attr('width', width)
    .attr('height', height);

  // vertical annotation bar
  svg.append('g')
    .attr('width', anonHeight)
    .attr('height', anonWidth)
    .attr('transform','translate('+ (axisWidth + margin.left + 20) +', ' + margin.top + ')')
  .selectAll('rect').data(colorCalibration).enter()
  .append('rect')
    .attr('width', anonHeight)
    .attr('height', anonRectWidth)
    .attr('y', function(d,i) {
      return i*anonRectWidth;
    })
    .attr('fill', function(d) {
      return d;
    });

  

  // add anontation y axis
  svg.append('g')
    .attr('transform','translate('+ (axisWidth + margin.left + 20 + anonHeight) +', ' + margin.top + ')')
    .attr('class','anontation y axis')
    .call(anonyAxis)
  .append("text")
    .style("text-anchor", "middle")
    .attr("transform", "rotate(90)")    
    .attr("dx", String(anonWidth/2))
    .attr("dy", "-5em")
    .text("Correlation Strength");

  // horizontal annotation bar
  svg.append('g')
    .attr('width', anonWidth)
    .attr('height', anonHeight)
    .attr('transform','translate('+ margin.left +', ' + (axisHeight + 2*margin.top) + ')')
    .selectAll('rect').data(horizontalCalibration).enter()
  .append('rect')
    .attr('width', anonWidth/horizontalCalibration.length)
    .attr('height', anonHeight)
    .attr('x', function(d,i) {
      return i*(anonWidth/horizontalCalibration.length);
    })
    .attr('fill', function(d) {
      return d;
    });

  // add anontation x axis
  svg.append('g')
    .attr('transform','translate('+ margin.left +', ' + (axisHeight + 2*margin.top + anonHeight) + ')')
    .attr('class','anontation x axis')
    .call(anonxAxis)
  .append("text")
    .style("text-anchor", "middle")  
    .attr("dx", String(anonWidth/2))
    .attr("dy", "5em")
    .text("Average Frequency Difference");

  var heatmap = svg
  .append('g')
    .attr('width',width-margin.left-margin.right)
    .attr('height',height-margin.top-margin.bottom)
    .attr('transform','translate('+margin.left+','+margin.top+')');

  var rect = null;

  d3.json('atom+xml.json', function(err, data){
    if (err) throw err;
    //console.log(data);
    //data = data.data;

    //render axises
    //~ xAxis.scale(xAxisScale.range([0,axisWidth]).domain([byteExtent[0],byteExtent[1]]));  
    xAxis.scale(xAxisScale.range([0,axisWidth]).domain([0, 255]));
    svg.append('g')
      .attr('transform','translate('+margin.left+','+margin.top+')')
      .attr('class','x axis')
      .call(xAxis);
    //~ .append('text')
      //~ .text('byte')
      //~ .attr('transform','translate('+axisWidth+',-10)');

    svg.append('g')
      .attr('transform','translate('+margin.left+','+margin.top+')')
      .attr('class','y axis')
      .call(yAxis);
    //~ .append('text')
      //~ .text('time')
      //~ .attr('transform','translate(-10,'+axisHeight+') rotate(-90)');

    //render heatmap rects
    //~ dayOffset = dayFormat(dateExtent[0]);
    rect = heatmap.selectAll('rect')
      .data(data).enter()
    .append('rect')
      .attr('width',cellSize)
      .attr('height',cellSize)
      .attr('x',function(d){
        //~ return itemSize*(dayFormat(d.date)-dayOffset);
        return itemSize*d.byte1;
      })
      .attr('y',function(d){
        //~ return hourFormat(d.date)*itemSize;
        return d.byte0*itemSize;
      })
      .attr('fill','#ffffff');

    rect.filter(function(d){ return d.value>0;})
      .append('title')
      .text(function(d){
        //~ return monthDayFormat(d.date)+' '+d.value['PM2.5'];
        return d.value;
      });

    renderColor();
  });

  //~ function initCalibration(){
    //~ d3.select('[role="calibration"] [role="example"]').select('svg')
      //~ .selectAll('rect').data(colorCalibration).enter()
    //~ .append('rect')
      //~ .attr('width',cellSize*100)
      //~ .attr('height',cellSize*10)
      //~ .attr('x',function(d,i){
        //~ return i*itemSize;
      //~ })
      //~ .attr('fill',function(d){
        //~ return d;
      //~ });
//~ 
    //~ //bind click event
    d3.selectAll('[role="calibration"] [name="displayType"]').on('click',function(){
      renderColor();
    });
  //~ }

  function renderColor(){
    //~ var renderByCount = document.getElementsByName('displayType')[0].checked;

    rect
      //~ .filter(function(d){
        //~ return (d.value['PM2.5']>=0);
      //~ })
      .transition()
      .delay(function(d){      
        //~ return (dayFormat(d.date)-dayOffset)*15;
        return d.byte1*15;
      })
      .duration(500)
      .attrTween('fill',function(d,i,a){
        //choose color dynamicly
        var colorIndex = d3.scale.quantize()
          .domain([0, 1])
          .range([0,1,2,3,4,5]);
          //~ .domain((renderByCount?[0,500]:dailyValueExtent[d.day]));

        return d3.interpolate(a,colorCalibration[colorIndex(d.value)]);
        //~ return d3.interpolate(a,colorCalibration[colorIndex(d.value['PM2.5'])]);
      });
  }
  
  //extend frame height in `http://bl.ocks.org/`
  d3.select(self.frameElement).style("height", "600px");  
})();
