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
            #plot_holder .button {
                position: absolute;
                cursor: pointer;
            }
            #plot_holder div.button {
                font-size: smaller;
                color: #999;
                background-color: #eee;
                padding: 2px;
            }
            .message {
                padding-left: 50px;
                font-size: smaller;
            }

            .site{
                overflow: hidden;
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
                            <li class="active"><a href="/site-checker">Home</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="row" id="sites_row">

            </div>
            <div class="row">
                <div class="span12">
                    <div id="plot_holder" style="width:100%;height:400px;">

                    </div>
                </div>
            </div>
        </div>

        <div id="site_template" style="display: none;">
            <div>
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

    </body>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.17.custom.min.js"></script>
    <script type="text/javascript" src="js/pure.js"></script>
    <script type="text/javascript" src="js/bootstrap.js"></script>
    <script type="text/javascript" src="js/less-1.2.1.min.js"></script>
    <script language="javascript" type="text/javascript" src="js/flot/jquery.flot.js"></script>
    <script language="javascript" type="text/javascript" src="js/flot/jquery.flot.navigate.js"></script>
    <script type="text/javascript">
        var slow_img = "img/17.png"
        var medium_img = "img/26.png"
        var normal_img = "img/32.png"
        var sites = {siteResponse: [{ site: 'http://www.google.com' }, {site: 'http://slashdot.org'}, {site: 'http://www.arstechnica.com'}]}
        var site_ids = [];
        var plot_timeout = false;
        var plot_obj
        var directives = {
            "p.link a": "site.url",
            "p.link a@href": "site.url",
            "div.alert span.responseCode":"code",
            "div.alert span.responseTime":"responseTime",
            "img.status@src": function(arg){
                if(arg.context.responseTime > 1000){
                    return slow_img
                }else if(arg.context.responseTime > 500){
                    return medium_img
                }else{
                    return normal_img
                }
            }
        }
        
        $(document).ready(function(){
            $.ajaxSetup({ cache: false });
            site_temp = $('#site_template div').compile(directives)   
            
            $.get("/site-checker/resources/site/", {}, function(resp){
                $.each(resp, function(i, site){
                    site_ids.push(site.id)
                    $("#sites_row").append("<div class='site span4' site='"+site.id+"'><div class='content'>Loading...</div></div>");
                    update(site.id)
                    var refresh = setInterval(function(){
                        update(site.id)
                    }, 10000)
                })
                $('div.site').click(function(){
                    plot($(this).attr('site'))
                })
            })
        })
        
        function update(site_id){
            $.ajax('resources/site/'+site_id+"/response/latest", {
                method: 'GET'
            }).success(function(resp){
                $('div[site=' + site_id + "] div").render(resp,site_temp)
                $('div[site=' + site_id + "] div").effect('highlight', {}, 2000)
            })
        }
        
        function plot(site_id){
            clearInterval(plot_timeout)            
            var options = {
                xaxis: { mode: "time" }
            };
            plot_obj = $.plot($("#plot_holder"), [getData(site_id)], options)
            function pltUpdate(){
                plot_obj.setData([getData(site_id)])
                plot_obj.setupGrid()
                plot_obj.draw()
            }            
            plot_timeout = setInterval(pltUpdate, 10000);
        }
        
        function getData(site_id){
            var start = new Date().getTime() - ((1000 * 60) * 10)
            var data = []
            $.ajax("resources/site/" + site_id + "/response", {
                async: false,
                method: "GET",
                params: { start: (start) }
            }).success(function(responses){
                    
                $.each(responses, function(i, resp){
                    if(resp.createdAt > start){
                        data.push([resp.createdAt, (resp.responseTime / 1000.0)])
                    }
                })
            })
            return data
                
        }
    </script>
</html>
