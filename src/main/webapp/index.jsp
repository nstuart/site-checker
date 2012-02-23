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
            .overlay{
                opacity: .0;
                background-image: url('img/ajax-loader.gif');
                background-repeat: no-repeat;
                background-color: #666;
                position: absolute;
                z-index: 10;
            }
            img.status{
                float: left; width: 32px; height: 32px;
            }
            p.link{
                float:left; padding-top: 8px; margin-bottom: 4px; overflow: hidden;
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
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="row">
                <div class="sites">

                </div>
            </div>
            <div class="row">
                <div class="span6">
                    <button class="btn btn-primary" onclick="update()">Update</button>
                </div>
            </div>

        </div>

        <div style="display: none;">
            <div id="site_template">
                <div class="content sites">
                    <div class="span4">
                        <img src="img/32.png" class="status" />
                        <p class="link"><a href=""></a></p>
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
        </div>

    </body>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.17.custom.min.js"></script>
    <script type="text/javascript" src="js/pure.js"></script>
    <script type="text/javascript" src="js/bootstrap.js"></script>
    <script type="text/javascript" src="js/less-1.2.1.min.js"></script>
    <script type="text/javascript">
        var slow_img = "img/17.png"
        var medium_img = "img/26.png"
        var normal_img = "img/32.png"
        var sites = {siteResponse: [{ site: 'http://www.google.com' }, {site: 'http://slashdot.org'}, {site: 'http://www.arstechnica.com'}]}
        var site_temp;
        var directives = {
            "div.span4":{
                "resp<-siteResponse":{
                    "p.link a": "resp.site",
                    "p.link a@href": "resp.site",
                    "div.alert span.responseCode":"resp.code",
                    "div.alert span.responseTime":"resp.responseTime",
                    "img.status@src": function(arg){
                        if(arg.item.responseTime > 1000){
                            return slow_img
                        }else if(arg.item.responseTime > 500){
                            return medium_img
                        }else{
                            return normal_img
                        }
                    }
                }
            }
        }
        
        $(document).ready(function(){
            update()
            site_temp = $('#site_template div.content').compile(directives)
            $('div.sites').render(sites,site_temp)
        })
        
        function update(){
            var urls = []
            
            $.each(sites.siteResponse, function(index, site){                
                urls.push(site.site)
            })
            
            $.ajax('resources/sitecheck', {
                method: 'GET',
                data: {urls: urls}
            }).success(function(resp){
                $('div.sites')
                .render(resp,site_temp)
            }).success(function(resp){
                
            })
        }
    </script>
</html>
