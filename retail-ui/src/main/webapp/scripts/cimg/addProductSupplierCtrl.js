/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('addProductSupplierCtrl', function($scope, baseDataService,ngDialog, productSupplierObject, SUCCESS, FAILURE, UNOM_ALL_URI) {

    populatePageData();
    function populatePageData() {
        $scope.productSupplierBackup = angular.copy(productSupplierObject);
        $scope.productSupplier = productSupplierObject;
        baseDataService.getBaseData(UNOM_ALL_URI).then(function(response){
            $scope.unitOfMeasureSet = response.data;
            $scope.productSupplier.suppUnitOfMeasure = baseDataService.populateSelectList($scope.productSupplier.suppUnitOfMeasure,$scope.unitOfMeasureSet);
        });

    }

    $scope.submit = function () {
        if ($scope.productSupplier != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.confirm($scope.productSupplier);
        }
    }
    $scope.cancel = function() {
        productSupplierObject = $scope.productSupplierBackup;
        $scope.closeThisDialog('button');
    }
    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.productSupplier.supplier = value;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

});
