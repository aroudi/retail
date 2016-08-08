/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('pdfViewerCtrl', function($scope, $state, $sce, pdfContent) {

    $scope.onInit=function() {

    }

    $scope.onPageLoad = function() {

    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

    $scope.trustSrc = function() {
        return $sce.trustAsResourceUrl(pdfContent);
    }
});
