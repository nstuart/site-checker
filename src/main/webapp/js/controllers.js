function SitesController($scope, $timeout, Site, LatestResponse, Plot){
    $scope.loadLatest = function(){
        var sites = $scope.sites
        console.log("loading latest response objects.")
        $.each(sites, function(i, site){
            LatestResponse.get({
                site_id: site.id
            }, function(nextLatest){
                if(!site.latest || site.latest.createdAt < nextLatest.createdAt){
                    site.latest = nextLatest
                    site.latest.statusImage = $scope.statusImage(site.latest)
                }
            })
        })
        $timeout($scope.loadLatest, 15000)
    }

    $scope.images = {
        slow_img: "img/17.png",
        medium_img: "img/26.png",
        normal_img: "img/32.png"
    }
    
    $scope.statusImage = function(latest){
        var img = 'normal_img'
        if(latest.code > 400){
            img = 'slow_img'
        }else if(latest.responseTime > 1000){
            img = 'slow_img'
        }else if(latest.responseTime > 500){
            img = 'medium_img'
        }
        return $scope.images[img]
    }
    
    $scope.sites = Site.query(function(sites){
        $.each(sites, function(i, site){
            //setup a listener 
            $scope.$watch('sites[' + i + '].latest', function(){
                console.log("Latest result changed for " + site.id)
            })
            $scope.loadLatest()
        })   
    })
    
    $scope.plotSite = function(site){
        $scope.$broadcast('plotChange', {
            site: site
        })
    }
}

PlotController = function($scope, Response){
    $scope.site = {}
    $scope.responses = {}
    $scope.start = new Date().getTime() - ((1000 * 60) * 60);
    $scope.data = []
    $scope.options = {
        xaxis: {
            mode: "time"
        },
        series: {
            lines: {
                show: true
            }
        },
        grid: {
            hoverable: true, 
            clickable: true
        }
    }
        
    $scope.$on('plotChange', function(event, args){
        var site = args.site
        console.log('plot site changed ' + site.id)
        $scope.site = site
        $scope.responses = Response.query({
            site_id: site.id
        },function(){
            $scope.loadData()
            var plot = $.plot($('#plot_holder'), [$scope.data] , $scope.options)
            $scope.plot = plot
        })
        
    })
    
    $scope.$watch('site.latest', function(newValue, oldValue){
        if(newValue != oldValue){
            console.log('latest plot element changed.')
            var resps = $scope.responses
            if(resps && resps.length > 0 && newValue && resps[resps.length - 1].createdAt != newValue.createdAt){
                console.log('appending new element.')
                $scope.data.data.push([newValue.createdAt, newValue.responseTime / 1000.0])
                $scope.plot.setData([$scope.data])
                $scope.plot.setupGrid()
                $scope.plot.draw()
            }
        }
    })
    
    $scope.loadData = function(){
        var data  = []
        $.each($scope.responses, function(i, resp){
            data.push([resp.createdAt, resp.responseTime / 1000.0])
        })
        
        $scope.data = {
            data: data, 
            label: $scope.site.url
            }
    }
    
}