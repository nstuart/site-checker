<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.css" type="text/css" rel="stylesheet"/>

        <style>
            body {
                padding-top: 60px; 
            }
        </style>
    </head>
    <body>
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="#">Site Checker</a>
                    <div class="nav-collapse">
                        <ul class="nav">
                            <li class="active"><a href="/jaxrs-site-checker">Home</a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>

        <div class="container">
            <div class="row">

                <div class="span3">
                    <div site="http://www.google.com">
                        <img src="img/32.png" style="float: left; width: 32px; height: 32px;"/>
                        <p style="float:left; padding-top: 8px; margin-bottom: 4px; overflow: hidden;"></p>
                        <div class="alert alert-success" style="clear: both;">
                            Status:
                            <span class="responseCode">
                                NONE
                            </span>
                            Time:
                            <span class="responseTime">
                                N/A
                            </span>ms
                        </div>
                    </div>
                </div>
                <div class="span3">
                    <div site="http://slashdot.org">
                        <img src="img/32.png" style="float: left; width: 32px; height: 32px;"/>
                        <p style="float:left; padding-top: 8px; margin-bottom: 4px; overflow: hidden;"></p>
                        <div class="alert alert-success" style="clear: both;">
                            Status:
                            <span class="responseCode">
                                NONE
                            </span>
                            Time:
                            <span class="responseTime">
                                N/A
                            </span>ms
                        </div>
                    </div>
                </div>

            </div>
            <div class="row">
                <div class="span6">
                    <button class="btn btn-primary" onclick="update()">Update</button>
                </div>
            </div>

        </div>
    </body>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.17.custom.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.js"></script>
    <script type="text/javascript" src="js/less-1.2.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            update()
        })
        function update(){
            $('div[site]').each(function(){
                var site = $(this).attr('site');
                $(this).find('p').html('<a href="' + site + '">' + site + '</a>');
                var div = this
                $.get('resources/sitecheck', {
                    url: site
                }).success(function(resp){
                    $(div).find('.alert')
                    .find('.responseCode').html(resp.code)
                    .end()
                    .find('.responseTime:first').html(resp.responseTime)
                }).success(function(){
                    $(div).find('p').effect('highlight', {}, 2000)
                })
            })
            
        }
    </script>
</html>
