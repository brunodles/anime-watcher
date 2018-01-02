// JS injection used to retrive Episodes on main page
// http://www.animesonlinebr.com.br/legendados/1532

var regex = /\d+/;
var text = ""
$(".lcp_catlist.list li a").each(function() {
  var element = $(this);
  text+="Episode(number="+parseInt(regex.exec(element.text()))
    + ", description=\""+element.text().split('-')[1].trim()
    + "\", link=\""+element.attr("href")
    +"\"),\n";

//   console.log(this);
})
console.log(text);