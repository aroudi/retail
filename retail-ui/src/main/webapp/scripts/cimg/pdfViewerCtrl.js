/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('pdfViewerCtrl', function($scope, $state, $sce, baseDataService) {

    /*
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
    */
    $scope.trustSrc = function() {
        return $sce.trustAsResourceUrl(baseDataService.getPdfContent());
    }
});
