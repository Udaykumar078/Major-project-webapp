

<%@page import="com.webapp.helper.ConnectionProvider"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        
        <!--css-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link href="css/mystyle.css" rel="stylesheet" type="text/css"/>
        
    </head>
    <body>
       
        <div class="container">
            <br>
            <br>
              <table class="table table-striped">
              <tbody>
                <tr>
                    <td><button class="btn btn-secondary " disabled id="btn1" onClick="myFunction1()">Get Next Contract</button></td>
                    <td><p type="text" hidden name="sno" id="snoid">1</p></td>
                  <td><p type="text" name="contract_id" id="contract_id" >0</p></td>
                  
                </tr>
              </tbody>
            </table>
            

        </div>
        <div class="container">
            <button class="btn btn-secondary" disabled id="btn2" onClick="myFunction2()">Get Participants</button>
        </div>
        
        <div class="container" id="test"></div>
        
        <div class="container">
            <button class="btn btn-secondary" disabled id="btn3" onClick="myFunction3()">Run</button>
        </div>
        
        <div class="container" id="rundiv" >
            
        </div>
        


 
            
        <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <script src="js/myjs.js" type="text/javascript"></script>
        <script>
                $(document).ready(function(){
                 $('#btn1').click(function(){
                 $('#btn1').prop('disabled',true);
                 $('#btn2').prop('disabled',false);
                   });
                   $('#btn2').click(function(){
                 $('#btn2').prop('disabled',true);
                 $('#btn3').prop('disabled',false);
                   });
                   $('#btn3').click(function(){
                 $('#btn3').prop('disabled',true);
                 $('#btn1').prop('disabled',false);
                   });
                 });
                $(document).ready(function(){
                    $.ajax({
                       url: "UtilityServlet",
                        type: 'GET',
                        success : function (data, textStatus, jqXHR){
                            alert("success");
                        },
                        error : function (jqXHR, textStatus, errorThrown){
                            console.log(jqXHR);
                            alert("failed error");
                        },
                        processData: false,
                        ContentType: false  
                    }); 
                    $('#btn1').prop('disabled',false);
                });
                
            function myFunction1(){
                var xhttp1;    
                  xhttp1 = new XMLHttpRequest();
                  xhttp1.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                      document.getElementById("contract_id").innerHTML = this.responseText;
                    }
                  };
                  var x = parseInt($("#snoid").text(),10);
                  xhttp1.open("GET", "Testing?sno="+x, true);
                  xhttp1.send();
                  document.getElementById("snoid").innerHTML=x+1;
            }
            function myFunction2(){
                 var xhttp2;    
                  xhttp2 = new XMLHttpRequest();
                  xhttp2.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                      document.getElementById("test").innerHTML = this.responseText;
                    }
                  };
                  var x = parseInt($("#contract_id").text(),10);
                  xhttp2.open("GET", "Testing2?contract_id="+x, true);
                  xhttp2.send();
                
            }function myFunction3(){
                 var xhttp3;    
                  xhttp3 = new XMLHttpRequest();
                  xhttp3.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                      document.getElementById("rundiv").innerHTML = this.responseText;
                    }
                  };
                  xhttp3.open("GET", "Testing3", true);
                  xhttp3.send();
                
            }
        </script>
        <script>
          /*  $(document).ready(function(){
                console.log("loaded.....");
                
                $('#cform').on('submit',function(event){
                    event.preventDefault();
                    
                    let form = new FormData(this);
                    
                     var x = parseInt($('#snoid').val() ,10);
                     console.log(x);
                    $.ajax({
                       url: "Testing2",
                        type: 'POST',
                        data: form,
                        success : function (data, textStatus, jqXHR){
                            console.log(data);
                            document.getElementById("contract_id").value = data;
                        },
                        error : function (jqXHR, textStatus, errorThrown){
                            console.log(jqXHR);
                        },
                        processData: false,
                        ContentType: false  
                    }); 
                    document.getElementById("snoid").value=x+1;
                    
                    $.ajax({
                       url: "Testing",
                        type: "POST",
                        data: form,
                        success : function (data, textStatus, jqXHR){
                            console.log(data);
                            document.getElementById("test").innerHTML = data;
                        },
                        error : function (jqXHR, textStatus, errorThrown){
                            console.log(jqXHR);
                        },
                        processData: false,
                        ContentType: true  
                    }); 
                   
                });
            });*/
        </script>
    </body>
</html>
