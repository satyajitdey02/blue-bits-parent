<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="resources/css/main.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<form id="urlShortenForm" method="POST" action="/urlshorten/api/us/shorten">
  <div class="urlShortenFormContainer">
    <input type="url" id="longUrl" class="longUrl" name="longUrl" placeholder="Enter Long URL"/>
    <input type="submit" id="shortenUrl" value="Shorten URL"/>
    <div class="shortUrl">
      <a class="shortLink" href="index.jsp" target="_blank"></a>
    </div>
  </div>
</form>

<script type="text/javascript">

  $(document).ready(function () {
    $('#urlShortenForm').submit(function (event) {
      $.ajax({
        url: $('#urlShortenForm').attr('action'),
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify({ "longUrl": $('#longUrl').val()}),
        processData: false,
        success: function (data) {
          if (data.shortUrl) {
            $("a.shortLink").attr("href", data.shortUrl);
            $("a.shortLink").text(data.shortUrl);
          } else {
            console.log("Error shortening URL");
          }
        },
        error: function (data) {
          console.log(data)
        }
      });

      event.preventDefault();
    });

  });
</script>
