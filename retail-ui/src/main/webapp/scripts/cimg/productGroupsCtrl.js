/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productGroupsCtrl', function($scope, baseDataService, SUCCESS, FAILURE, PRGP_GET_ALL_TREEVIEW_URI) {


    $scope.productGroupTreeOptions = {
        multipleSelect: true
    };

    $scope.$on('selection-changed', function(e, nodes) {
        $scope.selectedNodes = nodes;
    });

    initPage();
    function initPage() {
        baseDataService.getBaseData(PRGP_GET_ALL_TREEVIEW_URI).then(function(response){
            $scope.productGroupTree = response.data;
        });
    }
    $scope.submit = function () {
        $scope.confirm($scope.selectedNodes);
    }
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
