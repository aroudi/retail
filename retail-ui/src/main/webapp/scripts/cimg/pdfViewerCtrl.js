/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('pdfViewerCtrl', function($scope,url, $state, $sce, baseDataService) {

    /*
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
    */
    baseDataService.getStreamData(url).then(function(response){
        var blob = new Blob([response.data], {'type': 'application/pdf'});
        $scope.myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
        //return $sce.trustAsResourceUrl(myPdfContent);
    });
    $scope.trustSrc = function() {
        return $sce.trustAsResourceUrl($scope.myPdfContent);
    }
});
