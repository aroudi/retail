/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('pdfViewerCtrl', function($scope,url,searchForm, $state, $sce, baseDataService) {

    /*
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
    */
    if (searchForm == undefined) {
        baseDataService.getStreamData(url).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            $scope.myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            //return $sce.trustAsResourceUrl(myPdfContent);
        });
    } else {
        baseDataService.getStreamDataByPost(searchForm, url).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            $scope.myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
        });
    }
    $scope.trustSrc = function() {
        return $sce.trustAsResourceUrl($scope.myPdfContent);
    }
});
