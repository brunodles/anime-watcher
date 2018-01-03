// JS injection used to retrive Episodes on About Page
// http://www.animesorion.tv/260
// http://www.animesorion.tv/81318

var regex = /\d+/;
var text = ""
//reversed list
//$($("#listadeepisodios li a").get().reverse())each(function() {
$("#listadeepisodios li a").each(function() {
  var element = $(this);
  var texts = element.text().split("Episódio");
  var number = parseInt(regex.exec(texts[1].trim()))
  text+="Episode(number="+number
    + ", description=\"Episódio "+number
    + "\", animeName=\""+texts[0].trim()
    + "\", link=\""+element.attr("href")
    +"\"),\n";

//   console.log(this);
})
console.log(text);